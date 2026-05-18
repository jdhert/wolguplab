"use client";

import { useState } from "react";
import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowLeft, ArrowRight, RotateCcw } from "lucide-react";
import { useForm, type FieldPath, type UseFormReturn } from "react-hook-form";
import { cn } from "@/lib/utils";
import {
  lifestyleOptions,
  seoulDistricts,
  seoulLivingSchema,
  type SeoulLivingFormValues,
} from "./schema";
import { createMockSeoulLivingReport, formatKrw } from "./mock-report";
import type { SeoulLivingReport } from "./types";

type Step = {
  title: string;
  eyebrow: string;
  fields: FieldPath<SeoulLivingFormValues>[];
};

const steps: Step[] = [
  {
    eyebrow: "소득",
    title: "연봉은 얼마인가요?",
    fields: ["annualSalary"],
  },
  {
    eyebrow: "지역",
    title: "어느 지역에서 살아보고 싶나요?",
    fields: ["district"],
  },
  {
    eyebrow: "주거비",
    title: "보증금과 월세 범위를 알려주세요.",
    fields: ["deposit", "monthlyRent"],
  },
  {
    eyebrow: "생활",
    title: "한 달 생활비와 저축 목표는 어느 정도인가요?",
    fields: ["monthlyLivingCost", "monthlySavingGoal"],
  },
  {
    eyebrow: "스타일",
    title: "평소 소비 스타일에 가까운 쪽은요?",
    fields: ["lifestyle"],
  },
];

const defaultValues: SeoulLivingFormValues = {
  annualSalary: 3600,
  district: "마포구",
  deposit: 1000,
  monthlyRent: 70,
  monthlyLivingCost: 110,
  monthlySavingGoal: 30,
  lifestyle: "균형형",
};

export function SeoulLivingSimulator() {
  const [stepIndex, setStepIndex] = useState(0);
  const [isReportVisible, setIsReportVisible] = useState(false);
  const [report, setReport] = useState<SeoulLivingReport | null>(null);
  const form = useForm<SeoulLivingFormValues>({
    resolver: zodResolver(seoulLivingSchema),
    defaultValues,
    mode: "onTouched",
  });

  const currentStep = steps[stepIndex];
  const progress = ((stepIndex + 1) / steps.length) * 100;

  const goNext = async () => {
    const isValid = await form.trigger(currentStep.fields);
    if (!isValid) {
      return;
    }

    if (stepIndex === steps.length - 1) {
      setReport(createMockSeoulLivingReport(form.getValues()));
      setIsReportVisible(true);
      return;
    }

    setStepIndex((value) => value + 1);
  };

  const goBack = () => {
    setStepIndex((value) => Math.max(0, value - 1));
  };

  const restart = () => {
    form.reset(defaultValues);
    setStepIndex(0);
    setIsReportVisible(false);
    setReport(null);
  };

  if (isReportVisible && report) {
    return (
      <section className="grid gap-5 lg:grid-cols-[0.8fr_1.2fr]">
        <div className="rounded-lg border border-black/10 bg-white p-5 shadow-soft">
          <p className="text-sm font-bold text-mint">Mock Report</p>
          <h2 className="mt-3 text-3xl font-black text-ink">{report.grade}</h2>
          <p className="mt-3 text-base leading-7 text-ink/68">{report.headline}</p>
          <dl className="mt-6 grid gap-3">
            <ReportMetric label="희망 지역" value={report.districtLabel} />
            <ReportMetric label="예상 월 실수령" value={formatKrw(report.monthlyNetIncome)} />
            <ReportMetric label="주거비 비중" value={`${report.housingCostRatio}%`} />
            <ReportMetric label="예상 저축 가능액" value={formatKrw(report.expectedMonthlySavings)} />
          </dl>
          <button
            className="mt-6 inline-flex h-11 items-center gap-2 rounded-lg bg-ink px-4 text-sm font-bold text-white"
            onClick={restart}
            type="button"
          >
            <RotateCcw aria-hidden="true" className="h-4 w-4" />
            다시 실험하기
          </button>
        </div>

        <div className="grid gap-5">
          <ReportList title="주의 포인트" items={report.warnings} tone="coral" />
          <ReportList title="추천 행동" items={report.recommendations} tone="mint" />
        </div>
      </section>
    );
  }

  return (
    <section className="rounded-lg border border-black/10 bg-white p-5 shadow-soft sm:p-7">
      <div className="mb-8">
        <div className="flex items-center justify-between gap-4 text-sm font-semibold text-ink/60">
          <span>{currentStep.eyebrow}</span>
          <span>
            {stepIndex + 1} / {steps.length}
          </span>
        </div>
        <div className="mt-3 h-2 overflow-hidden rounded-full bg-black/[0.06]">
          <div className="h-full rounded-full bg-mint transition-all" style={{ width: `${progress}%` }} />
        </div>
      </div>

      <form className="space-y-8" onSubmit={(event) => event.preventDefault()}>
        <div>
          <h1 className="text-3xl font-black text-ink sm:text-4xl">{currentStep.title}</h1>
          <div className="mt-6">{renderStepFields(stepIndex, form)}</div>
        </div>

        <div className="flex items-center justify-between gap-3 border-t border-black/10 pt-5">
          <button
            className="inline-flex h-11 items-center gap-2 rounded-lg border border-black/10 px-4 text-sm font-bold text-ink disabled:cursor-not-allowed disabled:opacity-40"
            disabled={stepIndex === 0}
            onClick={goBack}
            type="button"
          >
            <ArrowLeft aria-hidden="true" className="h-4 w-4" />
            이전
          </button>
          <button
            className="inline-flex h-11 items-center gap-2 rounded-lg bg-mint px-4 text-sm font-bold text-white"
            onClick={goNext}
            type="button"
          >
            {stepIndex === steps.length - 1 ? "리포트 보기" : "다음"}
            <ArrowRight aria-hidden="true" className="h-4 w-4" />
          </button>
        </div>
      </form>
    </section>
  );
}

function renderStepFields(
  stepIndex: number,
  form: UseFormReturn<SeoulLivingFormValues>,
) {
  const errors = form.formState.errors;

  if (stepIndex === 0) {
    return (
      <NumberField
        error={errors.annualSalary?.message}
        label="세전 연봉"
        suffix="만원"
        {...form.register("annualSalary", { valueAsNumber: true })}
      />
    );
  }

  if (stepIndex === 1) {
    return (
      <label className="grid gap-2">
        <span className="text-sm font-bold text-ink">희망 지역</span>
        <select className="h-12 rounded-lg border border-black/10 bg-paper px-4" {...form.register("district")}>
          {seoulDistricts.map((district) => (
            <option key={district} value={district}>
              {district}
            </option>
          ))}
        </select>
        <FieldError message={errors.district?.message} />
      </label>
    );
  }

  if (stepIndex === 2) {
    return (
      <div className="grid gap-4 sm:grid-cols-2">
        <NumberField
          error={errors.deposit?.message}
          label="보유 보증금"
          suffix="만원"
          {...form.register("deposit", { valueAsNumber: true })}
        />
        <NumberField
          error={errors.monthlyRent?.message}
          label="감당 가능한 월세"
          suffix="만원"
          {...form.register("monthlyRent", { valueAsNumber: true })}
        />
      </div>
    );
  }

  if (stepIndex === 3) {
    return (
      <div className="grid gap-4 sm:grid-cols-2">
        <NumberField
          error={errors.monthlyLivingCost?.message}
          label="월 생활비"
          suffix="만원"
          {...form.register("monthlyLivingCost", { valueAsNumber: true })}
        />
        <NumberField
          error={errors.monthlySavingGoal?.message}
          label="월 저축 목표"
          suffix="만원"
          {...form.register("monthlySavingGoal", { valueAsNumber: true })}
        />
      </div>
    );
  }

  return (
    <div className="grid gap-3 sm:grid-cols-3">
      {lifestyleOptions.map((option) => (
        <label
          className="flex min-h-24 cursor-pointer flex-col justify-between rounded-lg border border-black/10 bg-paper p-4 has-[:checked]:border-mint has-[:checked]:bg-mint/10"
          key={option}
        >
          <span className="font-bold text-ink">{option}</span>
          <input className="sr-only" type="radio" value={option} {...form.register("lifestyle")} />
        </label>
      ))}
      <FieldError message={errors.lifestyle?.message} />
    </div>
  );
}

type NumberFieldProps = React.InputHTMLAttributes<HTMLInputElement> & {
  error?: string;
  label: string;
  suffix: string;
};

function NumberField({ error, label, suffix, ...props }: NumberFieldProps) {
  return (
    <label className="grid gap-2">
      <span className="text-sm font-bold text-ink">{label}</span>
      <span className="flex h-12 items-center rounded-lg border border-black/10 bg-paper px-4">
        <input className="min-w-0 flex-1 bg-transparent outline-none" min={0} type="number" {...props} />
        <span className="text-sm font-bold text-ink/55">{suffix}</span>
      </span>
      <FieldError message={error} />
    </label>
  );
}

function FieldError({ message }: { message?: string }) {
  if (!message) {
    return null;
  }

  return <span className="text-sm font-semibold text-coral">{message}</span>;
}

function ReportMetric({ label, value }: { label: string; value: string }) {
  return (
    <div className="flex items-center justify-between gap-4 rounded-lg bg-paper px-4 py-3">
      <dt className="text-sm font-semibold text-ink/60">{label}</dt>
      <dd className="text-base font-black text-ink">{value}</dd>
    </div>
  );
}

function ReportList({
  items,
  title,
  tone,
}: {
  items: string[];
  title: string;
  tone: "coral" | "mint";
}) {
  return (
    <div className="rounded-lg border border-black/10 bg-white p-5 shadow-soft">
      <h2 className="text-xl font-black text-ink">{title}</h2>
      <ul className="mt-4 grid gap-3">
        {items.map((item) => (
          <li className="flex gap-3 text-sm leading-6 text-ink/70" key={item}>
            <span className={cn("mt-2 h-2 w-2 shrink-0 rounded-full", tone === "mint" ? "bg-mint" : "bg-coral")} />
            {item}
          </li>
        ))}
      </ul>
    </div>
  );
}
