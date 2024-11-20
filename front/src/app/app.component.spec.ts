import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionService } from './services/session.service';

import { AppComponent } from './app.component';

describe('AppComponent', () => {
	let app: AppComponent;
	let fixture: ComponentFixture<AppComponent>;
	let sessionService: SessionService;
	let router: Router;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [AppComponent],
			imports: [
				RouterTestingModule.withRoutes([
					{ path: '', component: AppComponent } 
				]),
				HttpClientModule,
				MatToolbarModule
			],
			providers: [
				SessionService
			],
		}).compileComponents();

		fixture = TestBed.createComponent(AppComponent);
		app = fixture.componentInstance;
		sessionService = TestBed.inject(SessionService);
		router = TestBed.inject(Router);

		fixture.detectChanges();
	});

	describe('Unit Test Suites', () => {
		it('should create the app', () => {
			expect(app).toBeTruthy();
		});	
	});

	describe('Integration Test Suites', () => {
		it('should display the correct navigation links when connected', () => {
			jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(true));
			
			fixture.detectChanges();

			const links = fixture.nativeElement.querySelectorAll('.link');
			expect(links.length).toBe(3);
			expect(links[0].textContent).toBe('Sessions');
			expect(links[1].textContent).toBe('Account');
			expect(links[2].textContent).toBe('Logout');
		});

		it('should display the correct navigation links when disconnected', () => {
			jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(false));

			fixture.detectChanges();

			const notLoggedLinks = fixture.nativeElement.querySelectorAll('.link');
			expect(notLoggedLinks.length).toBe(2);
			expect(notLoggedLinks[0].textContent).toBe('Login');
			expect(notLoggedLinks[1].textContent).toBe('Register');
		});

		it('should call logout() when Logout link is clicked', () => {
			const navigateSpy = jest.spyOn(router, 'navigate');

			jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(true));
			fixture.detectChanges();

			const logoutLink = fixture.nativeElement.querySelector('.link:nth-child(3)');
			logoutLink.click();

			expect(sessionService.logOut).toHaveBeenCalled;
			expect(navigateSpy).toHaveBeenCalledWith(['']);
		});
	});
});
