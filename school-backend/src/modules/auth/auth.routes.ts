import { Router } from 'express';
import * as authController from './auth.controller';

const router = Router();

router.post('/send-otp', authController.sendOtpHandler);
router.post('/verify-otp', authController.verifyOtpHandler);
router.post('/register', authController.registerHandler);
router.post('/login', authController.loginHandler);
router.post('/forgot-password', authController.forgotPasswordHandler);
router.post('/refresh-token', authController.refreshTokenHandler);
router.get('/roles', authController.rolesHandler);

export default router;
