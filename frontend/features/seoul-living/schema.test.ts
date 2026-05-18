import { describe, expect, it } from "vitest";
import { seoulLivingSchema } from "./schema";

describe("seoulLivingSchema", () => {
  it("accepts valid seoul living inputs", () => {
    const result = seoulLivingSchema.safeParse({
      annualSalary: 3600,
      district: "마포구",
      deposit: 1000,
      monthlyRent: 70,
      monthlyLivingCost: 110,
      monthlySavingGoal: 30,
      lifestyle: "균형형",
    });

    expect(result.success).toBe(true);
  });

  it("rejects invalid annual salary", () => {
    const result = seoulLivingSchema.safeParse({
      annualSalary: 0,
      district: "마포구",
      deposit: 1000,
      monthlyRent: 70,
      monthlyLivingCost: 110,
      monthlySavingGoal: 30,
      lifestyle: "균형형",
    });

    expect(result.success).toBe(false);
  });
});
