import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
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
import { NgZone } from '@angular/core';
import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
    let component: RegisterComponent;
    let fixture: ComponentFixture<RegisterComponent>;
    let authService: AuthService;
    let router: Router;
    let ngZone: NgZone;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [RegisterComponent],
            providers: [AuthService],
            imports: [
                RouterTestingModule.withRoutes([
                    { path: 'login', component: RegisterComponent }
                ]),
                BrowserAnimationsModule,
                HttpClientModule,
                ReactiveFormsModule,  
                MatCardModule,
                MatFormFieldModule,
                MatIconModule,
                MatInputModule
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(RegisterComponent);
        component = fixture.componentInstance;
        authService = TestBed.inject(AuthService);
        router = TestBed.inject(Router);
        ngZone = TestBed.inject(NgZone);
        fixture.detectChanges();
    });

    describe('Unit Test Suites', () => {
        it('should create', () => {
            expect(component).toBeTruthy();
        });

        it('should mark first name as invalid if empty', () => {
            const firstNameInput = component.form.get('firstName');

            firstNameInput?.setValue('');
            expect(firstNameInput?.valid).toBeFalsy();
            expect(firstNameInput?.errors?.['required']).toBeTruthy();
        });

        it('should mark last name as invalid if empty', () => {
            const lastNameInput = component.form.get('lastName');

            lastNameInput?.setValue('');
            expect(lastNameInput?.valid).toBeFalsy();
            expect(lastNameInput?.errors?.['required']).toBeTruthy();
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
            const firstNameInput = component.form.get('firstName');
            const lastNameInput = component.form.get('lastName');
			const emailInput = component.form.get('email');
			const passwordInput = component.form.get('password');

            firstNameInput?.setValue('');
            lastNameInput?.setValue('');
			emailInput?.setValue('');
			passwordInput?.setValue('');
			fixture.detectChanges();

			const submitButton = fixture.nativeElement.querySelector(
				'button[type="submit"]'
			);
			expect(submitButton.disabled).toBeTruthy();
		});

        it('should enable submit button if form is valid', () => {
            const firstNameInput = component.form.get('firstName');
            const lastNameInput = component.form.get('lastName');            
			const emailInput = component.form.get('email');
			const passwordInput = component.form.get('password');

            firstNameInput?.setValue('FirstTest');
            lastNameInput?.setValue('LastTest');
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
            jest.spyOn(authService, 'register').mockReturnValue(of(void 0));
        });

        it('should call AuthService.register with correct data on form submission', () => {
            const firstNameInput = component.form.get('firstName');
            const lastNameInput = component.form.get('lastName');            
			const emailInput = component.form.get('email');
			const passwordInput = component.form.get('password');
        
            firstNameInput?.setValue('FirstTest');
            lastNameInput?.setValue('LastTest');
            emailInput?.setValue('test@gmail.com');
			passwordInput?.setValue('password');

            ngZone.run(() => {
				component.submit();
			});

            expect(authService.register).toHaveBeenLastCalledWith({
                firstName: 'FirstTest',
                lastName: 'LastTest',
                email: 'test@gmail.com',
                password: 'password'
            });
        });

        it('should redirect to /login after successful registration', () => {
            jest.spyOn(router, 'navigate');

            const firstNameInput = component.form.get('firstName');
            const lastNameInput = component.form.get('lastName');            
			const emailInput = component.form.get('email');
			const passwordInput = component.form.get('password');
            
            firstNameInput?.setValue('FirstTest');
            lastNameInput?.setValue('LastTest');
            emailInput?.setValue('test@gmail.com');
			passwordInput?.setValue('password');

            ngZone.run(() => {
				component.submit();
			});

			expect(router.navigate).toHaveBeenLastCalledWith(['/login']);
        });

        it('should set onError to true when registration fails', () => {
            const errorResponse = { message: 'Registration failed' };
            jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error(errorResponse.message))); 
        
            const firstNameInput = component.form.get('firstName');
            const lastNameInput = component.form.get('lastName');            
			const emailInput = component.form.get('email');
			const passwordInput = component.form.get('password');
            
            firstNameInput?.setValue('FirstTest');
            lastNameInput?.setValue('LastTest');
            emailInput?.setValue('test@gmail.com');
			passwordInput?.setValue('password');

            ngZone.run(() => {
				component.submit();
			});
			
			expect(component.onError).toBeTruthy();
        });
    });
});
