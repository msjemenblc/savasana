import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { UserService } from 'src/app/services/user.service';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { NgZone } from '@angular/core';
import { User } from 'src/app/interfaces/user.interface';

import { MeComponent } from './me.component';

describe('MeComponent', () => {
	let component: MeComponent;
	let fixture: ComponentFixture<MeComponent>;
	let userService: UserService;
	let sessionService: SessionService;
	let router: Router;
	let ngZone: NgZone;

	const mockUser: User = {
		id: 1,
		email: 'john.doe@example.com',
		lastName: 'Doe',
		firstName: 'John',
		admin: true,
		password: 'password',
		createdAt: new Date,
		updatedAt: new Date
	};
	
	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [MeComponent],
			imports: [
				RouterTestingModule.withRoutes([
					{ path: '', component: MeComponent }
				]),
				MatSnackBarModule,
				HttpClientModule,
				MatCardModule,
				MatFormFieldModule,
				MatIconModule,
				MatInputModule,
				NoopAnimationsModule
			],
			providers: [
				UserService,
				SessionService
			],
		}).compileComponents();

		fixture = TestBed.createComponent(MeComponent);
		component = fixture.componentInstance;
		userService = TestBed.inject(UserService);
		sessionService = TestBed.inject(SessionService);
		sessionService.sessionInformation = {
			token: 'fake-jwt-token',
			type: 'user',
			id: 1,
			username: 'fakeUser',
			firstName: 'Test',
			lastName: 'Test',
			admin: true
		};
		router = TestBed.inject(Router);
		ngZone = TestBed.inject(NgZone);

		fixture.detectChanges();
	});

	describe('Unit Test Suites', () => {
		it('should create', () => {
			expect(component).toBeTruthy();
		});
	});

	describe('Integration Test Suites', () => {
		it('should fetch and display user information on ngOnInit', () => {
			jest.spyOn(userService, 'getById').mockReturnValue(of(mockUser));

			component.ngOnInit();

			expect(userService.getById).toHaveBeenLastCalledWith(sessionService.sessionInformation?.id.toString());
		
			expect(component.user).toEqual(mockUser);

			fixture.detectChanges();

			const nameElement = fixture.nativeElement.querySelector('p:nth-of-type(1)');
			const emailElement = fixture.nativeElement.querySelector('p:nth-of-type(2)');

			expect(nameElement?.textContent).toBe(`Name: John DOE`);
			expect(emailElement?.textContent).toBe(`Email: john.doe@example.com`);
		});

		it('should navigate back', () => {
			const backSpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});

			component.back();

			expect(backSpy).toHaveBeenCalled();

			backSpy.mockRestore();
		});

		it('should delete the user account and logOut', fakeAsync(() => {
			jest.spyOn(userService, 'delete').mockReturnValue(of(null));

			const logOutSpy = jest.spyOn(sessionService, 'logOut').mockImplementation(() => {});

			ngZone.run(() => {
				component.delete();
			});

			tick(3000);

			expect(logOutSpy).toHaveBeenCalled();
		}));

		it("should delete the user account and redirect to '/' route", fakeAsync(() => {
			jest.spyOn(userService, 'delete').mockReturnValue(of(null));

			const navigateSpy = jest.spyOn(router, 'navigate');

			ngZone.run(() => {
				component.delete();
			});

			tick(3000);

			expect(router.navigate).toHaveBeenCalledWith(['/']);
		}));
	});
});
