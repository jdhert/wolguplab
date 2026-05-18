export type SeoulLivingRiskLevel = "LOW" | "MEDIUM" | "HIGH";

export type SeoulLivingReport = {
  grade: string;
  headline: string;
  districtLabel: string;
  riskLevel: SeoulLivingRiskLevel;
  monthlyNetIncome: number;
  housingCostRatio: number;
  expectedMonthlySavings: number;
  warnings: string[];
  recommendations: string[];
};
