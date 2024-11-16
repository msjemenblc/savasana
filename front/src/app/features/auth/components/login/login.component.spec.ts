import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { NgZone } from '@angular/core';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
	let component: LoginComponent;
	let fixture: ComponentFixture<LoginComponent>;
	let authService: AuthService;
	let sessionService: SessionService;
	let router: Router;
	let ngZone: NgZone;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [LoginComponent],
			providers: [SessionService, AuthService],
			imports: [
				RouterTestingModule.withRoutes([
					{ path: 'sessions', component: LoginComponent }
				]),
				BrowserAnimationsModule,
				HttpClientModule,
				MatCardModule,
				MatIconModule,
				MatFormFieldModule,
				MatInputModule,
				ReactiveFormsModule,
			],
		}).compileComponents();

		fixture = TestBed.createComponent(LoginComponent);
		component = fixture.componentInstance;
		authService = TestBed.inject(AuthService);
		sessionService = TestBed.inject(SessionService);
		router = TestBed.inject(Router);
		ngZone = TestBed.inject(NgZone);
		fixture.detectChanges();
	});

	describe('Unit Test Suites', () => {
		it('should create', () => {
			expect(component).toBeTruthy();
		});

		it('should mark email as invalid if empty', () => {
			const emailInput = component.form.get('email');

			emailInput?.setValue('');
			expect(emailInput?.valid).toBeFalsy();
			expect(emailInput?.errors?.['required']).toBeTruthy();
		});

		it('should mark password as invalid if empty', () => {
			const passwordInput = component.form.get('password');

			passwordInput?.setValue('');
			expect(passwordInput?.valid).toBeFalsy();
			expect(passwordInput?.errors?.['required']).toBeTruthy();
		});

		it('should invalidate email when format is incorrect', () => {
			const emailInput = component.form.get('email');

			// sans '@'
			emailInput?.setValue('test.com');
			fixture.detectChanges();
			expect(emailInput?.valid).toBeFalsy();
			expect(emailInput?.hasError('email')).toBeTruthy();

			// caractères invalides
			emailInput?.setValue('test@g....com');
			fixture.detectChanges();
			expect(emailInput?.valid).toBeFalsy();
			expect(emailInput?.hasError('email')).toBeTruthy();
		});

		it('should disable submit button if form is invalid', () => {
			const emailInput = component.form.get('email');
			const passwordInput = component.form.get('password');

			emailInput?.setValue('');
			passwordInput?.setValue('');
			fixture.detectChanges();

			const submitButton = fixture.nativeElement.querySelector(
				'button[type="submit"]'
			);
			expect(submitButton.disabled).toBeTruthy();
		});

		it('should enable submit button if form is valid', () => {
			const emailInput = component.form.get('email');
			const passwordInput = component.form.get('password');

			emailInput?.setValue('test@gmail.com');
			passwordInput?.setValue('password');
			fixture.detectChanges();

			const submitButton = fixture.nativeElement.querySelector(
				'button[type="submit"]'
			);
			expect(submitButton.disabled).toBeFalsy();
		});
	});

	describe('Integration Test Suites', () => {
		beforeEach(() => {
			jest.spyOn(authService, 'login').mockReturnValue(
				of({
					token: 'fake-jwt-token',
					type: 'user',
					id: 1,
					username: 'fakeUser',
					firstName: 'Test',
					lastName: 'Test',
					admin: false
				})
			);
		});

		it('should call AuthService.login with correct data on form submission', () => {
			const emailInput = component.form.get('email');
			const passwordInput = component.form.get('password');

			emailInput?.setValue('test@gmail.com');
			passwordInput?.setValue('password');

			ngZone.run(() => {
				component.submit();
			})

			expect(authService.login).toHaveBeenLastCalledWith({ email: 'test@gmail.com', password: 'password' });
		});

		it('should call SessionService.logIn with correct data after succesful login', () => {
			jest.spyOn(sessionService, 'logIn');

			const emailInput = component.form.get('email');
			const passwordInput = component.form.get('password');

			emailInput?.setValue('test@gmail.com');
			passwordInput?.setValue('password');

			ngZone.run(() => {
				component.submit();
			});

			expect(sessionService.logIn).toHaveBeenLastCalledWith({
				token: 'fake-jwt-token',
				type: 'user',
				id: 1,
				username: 'fakeUser',
				firstName: 'Test',
				lastName: 'Test',
				admin: false
			})
		});

		it('should redirect to /sessions after successful login', () => {
			jest.spyOn(router, 'navigate');

			const emailInput = component.form.get('email');
			const passwordInput = component.form.get('password');

			emailInput?.setValue('test@gmail.com');
			passwordInput?.setValue('password');

			ngZone.run(() => {
				component.submit();
			});

			expect(router.navigate).toHaveBeenLastCalledWith(['/sessions']);
		});

		it('should set onError to true when login fails', () => {
			const errorResponse = { message: 'Login failed' };
			jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error(errorResponse.message)));

			const emailInput = component.form.get('email');
			const passwordInput = component.form.get('password');

			emailInput?.setValue('test@gmail.com');
			passwordInput?.setValue('password');

			ngZone.run(() => {
				component.submit();
			});
			
			expect(component.onError).toBeTruthy();
		});
	});
});
