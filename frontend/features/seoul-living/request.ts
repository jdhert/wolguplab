import type { SeoulLivingFormValues } from "./schema";

const MANWON_TO_KRW = 10_000;

export type SeoulLivingSimulationRequest = {
  annualSalary: number;
  district: string;
  deposit: number;
  monthlyRent: number;
  monthlyLivingCost: number;
  monthlySavingGoal: number;
  lifestyle: string;
};

export function toSeoulLivingSimulationRequest(
  values: SeoulLivingFormValues,
): SeoulLivingSimulationRequest {
  return {
    annualSalary: toKrw(values.annualSalary),
    district: values.district,
    deposit: toKrw(values.deposit),
    monthlyRent: toKrw(values.monthlyRent),
    monthlyLivingCost: toKrw(values.monthlyLivingCost),
    monthlySavingGoal: toKrw(values.monthlySavingGoal),
    lifestyle: values.lifestyle,
  };
}

function toKrw(value: number) {
  return Math.round(value * MANWON_TO_KRW);
}
