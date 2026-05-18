import { SimulationCard } from "@/components/simulation-card";
import { simulationCards } from "@/features/simulations/simulation-cards";

export default function HomePage() {
  return (
    <main className="mx-auto flex min-h-screen w-full max-w-6xl flex-col px-5 py-8 sm:px-8 lg:py-12">
      <header className="flex flex-col gap-6 border-b border-black/10 pb-8 lg:flex-row lg:items-end lg:justify-between">
        <div className="max-w-2xl">
          <p className="text-sm font-bold text-mint">월급실험실</p>
          <h1 className="mt-3 text-4xl font-black tracking-normal text-ink sm:text-5xl">
            월급으로 가능한 선택지를 먼저 실험해보세요
          </h1>
          <p className="mt-4 text-base leading-7 text-ink/68">
            연봉, 주거비, 생활 습관을 바탕으로 현실적인 생활 리포트를 만듭니다.
          </p>
        </div>
        <div className="rounded-lg border border-black/10 bg-white px-4 py-3 text-sm font-semibold text-ink/70 shadow-soft">
          MVP에서는 서울 자취 가능성부터 열려 있습니다.
        </div>
      </header>

      <section className="grid gap-4 py-8 sm:grid-cols-2 lg:grid-cols-3">
        {simulationCards.map((card) => (
          <SimulationCard card={card} key={card.title} />
        ))}
      </section>
    </main>
  );
}
