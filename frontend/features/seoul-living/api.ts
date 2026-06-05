import type { SeoulLivingFormValues } from "./schema";
import { toSeoulLivingSimulationRequest } from "./request";
import type { SeoulLivingReport, SeoulLivingRiskLevel } from "./types";

const SEOUL_LIVING_SIMULATION_PATH = "/api/simulations/seoul-living";

export type SeoulLivingApiFieldError = {
  field: string;
  message: string;
};

type SeoulLivingApiErrorOptions = {
  fieldErrors?: SeoulLivingApiFieldError[];
  status?: number;
};

type SeoulLivingApiErrorResponse = {
  code?: string;
  message?: string;
  fieldErrors?: SeoulLivingApiFieldError[];
};

export class SeoulLivingApiError extends Error {
  readonly fieldErrors: SeoulLivingApiFieldError[];
  readonly status?: number;

  constructor(message: string, options: SeoulLivingApiErrorOptions = {}) {
    super(message);
    this.name = "SeoulLivingApiError";
    this.fieldErrors = options.fieldErrors ?? [];
    this.status = options.status;
  }
}

export async function createSeoulLivingReport(
  values: SeoulLivingFormValues,
  fetcher: typeof fetch = fetch,
): Promise<SeoulLivingReport> {
  const response = await fetcher(buildBackendApiUrl(SEOUL_LIVING_SIMULATION_PATH), {
    body: JSON.stringify(toSeoulLivingSimulationRequest(values)),
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
  }).catch(() => {
    throw new SeoulLivingApiError("서비스와 연결하지 못했습니다. 잠시 후 다시 시도해주세요.");
  });

  if (!response.ok) {
    throw await toApiError(response);
  }

  return parseSeoulLivingReport(await readJson(response));
}

function buildBackendApiUrl(path: string) {
  const baseUrl = process.env.NEXT_PUBLIC_BACKEND_API_BASE_URL?.trim();

  if (!baseUrl) {
    return path;
  }

  return new URL(path, ensureTrailingSlash(baseUrl)).toString();
}

function ensureTrailingSlash(value: string) {
  return value.endsWith("/") ? value : `${value}/`;
}

async function toApiError(response: Response) {
  const body = await readJson(response).catch(() => null);
  const apiError = parseApiErrorResponse(body);

  if (response.status === 400) {
    return new SeoulLivingApiError(apiError?.message ?? "입력값을 확인해주세요.", {
      fieldErrors: apiError?.fieldErrors,
      status: response.status,
    });
  }

  return new SeoulLivingApiError("서비스 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", {
    status: response.status,
  });
}

async function readJson(response: Response): Promise<unknown> {
  return response.json();
}

function parseApiErrorResponse(value: unknown): SeoulLivingApiErrorResponse | null {
  if (!isRecord(value)) {
    return null;
  }

  return {
    code: typeof value.code === "string" ? value.code : undefined,
    message: typeof value.message === "string" ? value.message : undefined,
    fieldErrors: parseFieldErrors(value.fieldErrors),
  };
}

function parseFieldErrors(value: unknown) {
  if (!Array.isArray(value)) {
    return undefined;
  }

  return value.filter(isApiFieldError);
}

function isApiFieldError(value: unknown): value is SeoulLivingApiFieldError {
  return isRecord(value) && typeof value.field === "string" && typeof value.message === "string";
}

function parseSeoulLivingReport(value: unknown): SeoulLivingReport {
  if (!isRecord(value)) {
    throw invalidResponseError();
  }

  const riskLevel = value.riskLevel;

  if (!isRiskLevel(riskLevel)) {
    throw invalidResponseError();
  }

  if (
    typeof value.grade !== "string" ||
    typeof value.headline !== "string" ||
    typeof value.districtLabel !== "string" ||
    typeof value.monthlyNetIncome !== "number" ||
    typeof value.housingCostRatio !== "number" ||
    typeof value.expectedMonthlySavings !== "number" ||
    !isStringArray(value.warnings) ||
    !isStringArray(value.recommendations)
  ) {
    throw invalidResponseError();
  }

  return {
    grade: value.grade,
    headline: value.headline,
    districtLabel: value.districtLabel,
    riskLevel,
    monthlyNetIncome: value.monthlyNetIncome,
    housingCostRatio: value.housingCostRatio,
    expectedMonthlySavings: value.expectedMonthlySavings,
    warnings: value.warnings,
    recommendations: value.recommendations,
  };
}

function invalidResponseError() {
  return new SeoulLivingApiError("서비스 응답이 올바르지 않습니다. 잠시 후 다시 시도해주세요.");
}

function isRiskLevel(value: unknown): value is SeoulLivingRiskLevel {
  return value === "LOW" || value === "MEDIUM" || value === "HIGH";
}

function isStringArray(value: unknown): value is string[] {
  return Array.isArray(value) && value.every((item) => typeof item === "string");
}

function isRecord(value: unknown): value is Record<string, unknown> {
  return typeof value === "object" && value !== null;
}
