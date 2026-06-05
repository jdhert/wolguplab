import { describe, expect, it } from "vitest";
import { toSeoulLivingSimulationRequest } from "./request";

describe("toSeoulLivingSimulationRequest", () => {
  it("converts form money values from 만원 to KRW integers", () => {
    expect(
      toSeoulLivingSimulationRequest({
        annualSalary: 6000,
        district: "마포구",
        deposit: 1000,
        monthlyRent: 90,
        monthlyLivingCost: 150,
        monthlySavingGoal: 70,
        lifestyle: "균형형",
      }),
    ).toEqual({
      annualSalary: 60_000_000,
      district: "마포구",
      deposit: 10_000_000,
      monthlyRent: 900_000,
      monthlyLivingCost: 1_500_000,
      monthlySavingGoal: 700_000,
      lifestyle: "균형형",
    });
  });

  it("rounds converted KRW values to integers", () => {
    expect(
      toSeoulLivingSimulationRequest({
        annualSalary: 3600.1234,
        district: "성동구",
        deposit: 500.5555,
        monthlyRent: 72.3456,
        monthlyLivingCost: 111.1111,
        monthlySavingGoal: 30.0001,
        lifestyle: "절약형",
      }),
    ).toMatchObject({
      annualSalary: 36_001_234,
      deposit: 5_005_555,
      monthlyRent: 723_456,
      monthlyLivingCost: 1_111_111,
      monthlySavingGoal: 300_001,
    });
  });
});
