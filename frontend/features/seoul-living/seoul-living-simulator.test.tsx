import "@testing-library/jest-dom/vitest";

import React from "react";
import { cleanup, fireEvent, render, screen, waitFor } from "@testing-library/react";
import { afterEach, describe, expect, it, vi } from "vitest";
import { SeoulLivingSimulator } from "./seoul-living-simulator";

const successReport = {
  grade: "가능",
  headline: "현재 조건에서는 서울 자취가 비교적 안정적입니다.",
  districtLabel: "마포구",
  riskLevel: "LOW",
  monthlyNetIncome: 4_100_000,
  housingCostRatio: 22,
  expectedMonthlySavings: 1_700_000,
  warnings: ["현재 입력 조건에서 즉시 확인되는 주요 위험 신호는 낮습니다."],
  recommendations: [
    "현재 수준을 유지하되 비상금과 이사 초기 비용을 별도로 준비하세요.",
    "균형형 생활을 유지하려면 월세와 저축 목표를 함께 관리하세요.",
  ],
};

afterEach(() => {
  cleanup();
  vi.unstubAllGlobals();
});

describe("SeoulLivingSimulator API integration", () => {
  it("submits converted values and renders the backend report", async () => {
    const fetchMock = vi.fn<typeof fetch>(() =>
      Promise.resolve(
        new Response(JSON.stringify(successReport), {
          headers: { "Content-Type": "application/json" },
          status: 200,
        }),
      ),
    );
    vi.stubGlobal("fetch", fetchMock);

    render(<SeoulLivingSimulator />);
    await submitDefaultForm();

    await waitFor(() => expect(fetchMock).toHaveBeenCalledTimes(1));
    expect(fetchMock).toHaveBeenCalledWith(
      "/api/simulations/seoul-living",
      expect.objectContaining({
        body: JSON.stringify({
          annualSalary: 36_000_000,
          district: "마포구",
          deposit: 10_000_000,
          monthlyRent: 700_000,
          monthlyLivingCost: 1_100_000,
          monthlySavingGoal: 300_000,
          lifestyle: "균형형",
        }),
        method: "POST",
      }),
    );
    expect(await screen.findByText("생활 리포트")).toBeInTheDocument();
    expect(screen.getByText("가능")).toBeInTheDocument();
    expect(screen.getByText("₩4,100,000")).toBeInTheDocument();
  });

  it("keeps the form open and shows API validation errors", async () => {
    const fetchMock = vi.fn<typeof fetch>(() =>
      Promise.resolve(
        new Response(
          JSON.stringify({
            code: "VALIDATION_ERROR",
            message: "입력값을 확인해주세요.",
            fieldErrors: [{ field: "annualSalary", message: "연봉을 입력해주세요." }],
          }),
          {
            headers: { "Content-Type": "application/json" },
            status: 400,
          },
        ),
      ),
    );
    vi.stubGlobal("fetch", fetchMock);

    render(<SeoulLivingSimulator />);
    await submitDefaultForm();

    expect(await screen.findByRole("alert")).toHaveTextContent("입력값을 확인해주세요.");
    expect(screen.getByText("연봉을 입력해주세요.")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /다시 시도/ })).toBeEnabled();
    expect(screen.queryByText("생활 리포트")).not.toBeInTheDocument();
  });
});

async function submitDefaultForm() {
  const nextStepHeadings = [
    "어느 지역에서 살아보고 싶나요?",
    "보증금과 월세 범위를 알려주세요.",
    "한 달 생활비와 저축 목표는 어느 정도인가요?",
    "평소 소비 스타일에 가까운 쪽은요?",
  ];

  for (const heading of nextStepHeadings) {
    fireEvent.click(screen.getByRole("button", { name: /다음/ }));
    expect(await screen.findByRole("heading", { name: heading })).toBeInTheDocument();
  }

  await waitFor(() => expect(screen.getByRole("button", { name: /리포트 보기/ })).toBeEnabled());
  fireEvent.click(screen.getByRole("button", { name: /리포트 보기/ }));
}
