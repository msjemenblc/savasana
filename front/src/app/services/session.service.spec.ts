import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
	let service: SessionService;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(SessionService);
	});

	describe('Unit Test Suites', () => {
		it('should be created', () => {
			expect(service).toBeTruthy();
		});

		it('should update sessionInformation correctly on logIn', () => {
			const user: SessionInformation = {
				token: 'fake-jwt-token',
				type: 'user',
				id: 1,
				username: 'fakeUser',
				firstName: 'Test',
				lastName: 'Test',
				admin: false
			};

			service.logIn(user);

			expect(service.sessionInformation).toEqual(user);
			expect(service.isLogged).toBeTruthy();
		});

		it('should clear sessionInformation on logOut', () => {
			service.logIn({
				token: 'fake-jwt-token',
				type: 'user',
				id: 1,
				username: 'fakeUser',
				firstName: 'Test',
				lastName: 'Test',
				admin: false
			});

			service.logOut();

			expect(service.sessionInformation).toBeUndefined();
			expect(service.isLogged).toBeFalsy();
		});
	});
});
