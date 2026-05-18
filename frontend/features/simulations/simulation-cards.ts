import {
  BadgeHelp,
  Car,
  Home,
  Landmark,
  PiggyBank,
  ReceiptText,
} from "lucide-react";
import type { SimulationCard } from "./types";

export const simulationCards: SimulationCard[] = [
  {
    title: "서울 자취 가능성",
    description: "연봉, 보증금, 월세, 생활비로 독립 가능성을 살펴봅니다.",
    href: "/simulations/seoul-living",
    status: "available",
    icon: Home,
    accent: "mint",
  },
  {
    title: "퇴사 생존 분석",
    description: "현재 소비 습관으로 퇴사 후 버틸 수 있는 기간을 봅니다.",
    href: "/simulations/resignation",
    status: "coming-soon",
    icon: PiggyBank,
    accent: "coral",
  },
  {
    title: "자차 유지 가능성",
    description: "할부, 보험, 유류비가 월 현금흐름에 미치는 영향을 봅니다.",
    href: "/simulations/car",
    status: "coming-soon",
    icon: Car,
    accent: "grape",
  },
  {
    title: "월세 vs 전세 비교",
    description: "지역과 한도 기준으로 어떤 주거 선택이 나은지 비교합니다.",
    href: "/simulations/rent-vs-jeonse",
    status: "coming-soon",
    icon: ReceiptText,
    accent: "gold",
  },
  {
    title: "청약 현실성 분석",
    description: "소득, 자산, 지역 조건에서 청약 준비의 의미를 봅니다.",
    href: "/simulations/subscription",
    status: "coming-soon",
    icon: Landmark,
    accent: "mint",
  },
  {
    title: "지원금 추천",
    description: "조건에 따라 확인해볼 만한 지원금 후보를 정리합니다.",
    href: "/simulations/government-support",
    status: "coming-soon",
    icon: BadgeHelp,
    accent: "coral",
  },
];
