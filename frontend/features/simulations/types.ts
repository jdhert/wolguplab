import type { LucideIcon } from "lucide-react";

export type SimulationStatus = "available" | "coming-soon";

export type SimulationCard = {
  title: string;
  description: string;
  href: string;
  status: SimulationStatus;
  icon: LucideIcon;
  accent: "mint" | "coral" | "grape" | "gold";
};
