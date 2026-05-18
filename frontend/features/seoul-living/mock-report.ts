import type { SeoulLivingFormValues } from "./schema";
import type { SeoulLivingReport } from "./types";

export function createMockSeoulLivingReport(values: SeoulLivingFormValues): SeoulLivingReport {
  return {
    grade: "주의 필요",
    headline: "독립은 가능하지만 월세 비중을 먼저 낮추는 편이 좋습니다.",
    districtLabel: values.district,
    riskLevel: "MEDIUM",
    monthlyNetIncome: 2860000,
    housingCostRatio: 34,
    expectedMonthlySavings: 320000,
    warnings: [
      "월세와 생활비를 함께 보면 여유 현금흐름이 크지 않습니다.",
      "초기 가전, 이사비, 중개보수 같은 일회성 비용을 별도로 잡아야 합니다.",
    ],
    recommendations: [
      "희망 지역 주변 구까지 후보를 넓히면 선택지가 좋아집니다.",
      "최소 3개월치 생활비를 비상금으로 확보한 뒤 독립을 검토하세요.",
    ],
  };
}

export function formatKrw(value: number) {
  return new Intl.NumberFormat("ko-KR", {
    style: "currency",
    currency: "KRW",
    maximumFractionDigits: 0,
  }).format(value);
}
