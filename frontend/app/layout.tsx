import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "월급실험실",
  description: "월급과 생활 조건으로 현실적인 선택지를 시뮬레이션합니다.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body>{children}</body>
    </html>
  );
}
