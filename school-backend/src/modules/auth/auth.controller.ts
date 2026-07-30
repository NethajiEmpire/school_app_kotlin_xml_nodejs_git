import { Request, Response } from 'express';
import { asyncHandler } from '../../utils/asyncHandler';
import { ok, created } from '../../utils/apiResponse';
import * as authService from './auth.service';
import { AppError } from '../../utils/AppError';

export const sendOtpHandler = asyncHandler(async (req: Request, res: Response) => {
  const { mobile, purpose } = req.body;
  if (!mobile) throw new AppError(400, 'mobile is required');
  const result = await authService.sendOtp(req.db!, mobile, purpose || 'register');
  return ok(res, result, 'OTP sent successfully');
});

export const verifyOtpHandler = asyncHandler(async (req: Request, res: Response) => {
  const { mobile, otp } = req.body;
  if (!mobile || !otp) throw new AppError(400, 'mobile and otp are required');
  await authService.verifyOtp(req.db!, mobile, otp);
  return ok(res, null, 'OTP verified successfully');
});

export const registerHandler = asyncHandler(async (req: Request, res: Response) => {
  const { name, mobile, email, password, roleName } = req.body;
  if (!name || !mobile || !password || !roleName) {
    throw new AppError(400, 'name, mobile, password and roleName are required');
  }
  const result = await authService.register(req.db!, req.school!, { name, mobile, email, password, roleName });
  return created(res, result, 'Registered successfully');
});

export const loginHandler = asyncHandler(async (req: Request, res: Response) => {
  const { mobile, email, password } = req.body;
  const identifier = mobile || email;
  if (!identifier || !password) throw new AppError(400, 'mobile/email and password are required');
  const result = await authService.login(req.db!, req.school!, identifier, password);
  return ok(res, result, 'Login successful');
});

export const forgotPasswordHandler = asyncHandler(async (req: Request, res: Response) => {
  const { mobile, newPassword } = req.body;
  if (!mobile || !newPassword) throw new AppError(400, 'mobile and newPassword are required');
  await authService.forgotPassword(req.db!, mobile, newPassword);
  return ok(res, null, 'Password reset successfully');
});

export const refreshTokenHandler = asyncHandler(async (req: Request, res: Response) => {
  const { refreshToken } = req.body;
  if (!refreshToken) throw new AppError(400, 'refreshToken is required');
  const result = await authService.refreshSession(req.db!, req.school!, refreshToken);
  return ok(res, result, 'Session refreshed');
});

export const rolesHandler = asyncHandler(async (req: Request, res: Response) => {
  const roles = await authService.getRoles(req.db!);
  return ok(res, roles);
});
