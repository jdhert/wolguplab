import Link from "next/link";
import { ArrowRight, Lock } from "lucide-react";
import { cn } from "@/lib/utils";
import type { SimulationCard as SimulationCardType } from "@/features/simulations/types";

const accentClassName: Record<SimulationCardType["accent"], string> = {
  mint: "bg-mint/10 text-mint",
  coral: "bg-coral/10 text-coral",
  grape: "bg-grape/10 text-grape",
  gold: "bg-gold/10 text-gold",
};

type SimulationCardProps = {
  card: SimulationCardType;
};

export function SimulationCard({ card }: SimulationCardProps) {
  const Icon = card.icon;
  const isAvailable = card.status === "available";
  const content = (
    <div
      className={cn(
        "group flex h-full min-h-48 flex-col justify-between rounded-lg border border-black/10 bg-white p-5 shadow-soft transition",
        isAvailable ? "hover:-translate-y-1 hover:border-mint/40" : "opacity-70",
      )}
    >
      <div className="space-y-4">
        <div className="flex items-start justify-between gap-4">
          <div className={cn("grid h-11 w-11 place-items-center rounded-lg", accentClassName[card.accent])}>
            <Icon aria-hidden="true" className="h-5 w-5" />
          </div>
          <span className="rounded-full bg-black/[0.04] px-3 py-1 text-xs font-semibold text-ink/60">
            {isAvailable ? "시작" : "준비 중"}
          </span>
        </div>
        <div>
          <h2 className="text-xl font-bold text-ink">{card.title}</h2>
          <p className="mt-2 text-sm leading-6 text-ink/66">{card.description}</p>
        </div>
      </div>
      <div className="mt-6 flex items-center gap-2 text-sm font-semibold text-ink">
        {isAvailable ? (
          <>
            열어보기 <ArrowRight aria-hidden="true" className="h-4 w-4 transition group-hover:translate-x-1" />
          </>
        ) : (
          <>
            곧 추가됨 <Lock aria-hidden="true" className="h-4 w-4" />
          </>
        )}
      </div>
    </div>
  );

  if (!isAvailable) {
    return <div aria-disabled="true">{content}</div>;
  }

  return (
    <Link aria-label={`${card.title} 시뮬레이션 열기`} href={card.href}>
      {content}
    </Link>
  );
}
