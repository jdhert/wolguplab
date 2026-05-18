import { z } from "zod";

export const seoulDistricts = [
  "강남구",
  "강동구",
  "강북구",
  "강서구",
  "관악구",
  "광진구",
  "구로구",
  "금천구",
  "노원구",
  "도봉구",
  "동대문구",
  "동작구",
  "마포구",
  "서대문구",
  "서초구",
  "성동구",
  "성북구",
  "송파구",
  "양천구",
  "영등포구",
  "용산구",
  "은평구",
  "종로구",
  "중구",
  "중랑구",
] as const;

export const lifestyleOptions = ["절약형", "균형형", "여유형"] as const;

export const seoulLivingSchema = z.object({
  annualSalary: z.coerce.number().min(1, "연봉을 입력해주세요."),
  district: z.enum(seoulDistricts, {
    required_error: "희망 지역을 선택해주세요.",
  }),
  deposit: z.coerce.number().min(0, "보증금은 0원 이상이어야 합니다."),
  monthlyRent: z.coerce.number().min(0, "월세는 0원 이상이어야 합니다."),
  monthlyLivingCost: z.coerce.number().min(0, "생활비는 0원 이상이어야 합니다."),
  monthlySavingGoal: z.coerce.number().min(0, "저축 목표는 0원 이상이어야 합니다."),
  lifestyle: z.enum(lifestyleOptions, {
    required_error: "생활 스타일을 선택해주세요.",
  }),
});

export type SeoulLivingFormValues = z.infer<typeof seoulLivingSchema>;
