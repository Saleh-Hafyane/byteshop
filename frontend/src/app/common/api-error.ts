// Shape of the JSON error bodies the backend's GlobalExceptionHandler returns.
export interface ApiError {
  status: number; // HTTP status, e.g. 400 or 409
  message: string; // human-readable summary shown in alerts
  timestamp: string; // when the error happened
  errors?: Record<string, string>; // per-field messages, e.g. { email: "Email already in use" }
}
