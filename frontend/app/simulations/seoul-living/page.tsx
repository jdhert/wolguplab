import Link from "next/link";
import { ArrowLeft } from "lucide-react";
import { SeoulLivingSimulator } from "@/features/seoul-living/seoul-living-simulator";

export default function SeoulLivingPage() {
  return (
    <main className="mx-auto min-h-screen w-full max-w-5xl px-5 py-8 sm:px-8 lg:py-12">
      <Link className="inline-flex items-center gap-2 text-sm font-bold text-ink/70 hover:text-ink" href="/">
        <ArrowLeft aria-hidden="true" className="h-4 w-4" />
        시뮬레이션 선택
      </Link>
      <header className="max-w-3xl py-8">
        <p className="text-sm font-bold text-mint">서울 자취 가능성</p>
        <h1 className="mt-3 text-4xl font-black text-ink sm:text-5xl">지금 월급으로 독립해도 괜찮을까요?</h1>
        <p className="mt-4 text-base leading-7 text-ink/68">
          몇 가지 조건만 넣고, 생활 리포트 형태로 가능성을 먼저 확인합니다.
        </p>
      </header>
      <SeoulLivingSimulator />
    </main>
  );
}
