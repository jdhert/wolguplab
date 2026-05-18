import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./features/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        ink: "#1f2933",
        paper: "#f8faf7",
        mint: "#2f9e8f",
        coral: "#f26b5e",
        grape: "#6f5bd3",
        gold: "#d19b32",
      },
      boxShadow: {
        soft: "0 18px 50px rgba(31, 41, 51, 0.08)",
      },
    },
  },
  plugins: [],
};

export default config;
