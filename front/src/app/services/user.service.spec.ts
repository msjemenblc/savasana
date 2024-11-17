import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
	let service: UserService;
	let httpMock: HttpTestingController;

	const mockUser: User = {
		id: 1,
		email: 'test@gmail.com',
		lastName: 'LastTest',
		firstName: 'FirstTest',
		admin: false,
		password: 'password',
		createdAt: new Date,
		updatedAt: new Date
	};

	beforeEach(() => {
		TestBed.configureTestingModule({
			imports: [
				HttpClientTestingModule
			],
			providers: [
				UserService
			]
		});

		service = TestBed.inject(UserService);
		httpMock = TestBed.inject(HttpTestingController);
	});

	afterEach(() => {
		httpMock.verify();
	});

	describe('Unit Test Suites', () => {
		it('should be created', () => {
			expect(service).toBeTruthy();
		});

		it('should fetch a user by ID', () => {
			service.getById('1').subscribe((user) => {
				expect(user).toEqual(mockUser);
			});

			const req = httpMock.expectOne('api/user/1');
			expect(req.request.method).toBe('GET');
			req.flush(mockUser);
		});

		it('should handle 404 error when user not found', (done) => {
			service.getById('999').subscribe({
				next: () => fail('Expected an error, not a user'),
				error: (error) => {
					expect(error.status).toBe(404);
					done();
				}
			});

			const req = httpMock.expectOne('api/user/999');
			expect(req.request.method).toBe('GET');
			req.flush({ message: 'User not found' }, { status: 404, statusText: 'Not Found' });
		});

		it('should delete a user by ID', () => {
			service.delete('1').subscribe((response) => {
				expect(response).toEqual({ message: 'User deleted' });
			});

			const req = httpMock.expectOne('api/user/1');
			expect(req.request.method).toBe('DELETE');
			req.flush({ message: 'User deleted' });
		});

		it('should handle 404 error when trying to delete a non-existent user', (done) => {
			service.delete('999').subscribe({
				next: () => fail('Expected an error, not a success response'),
				error: (error) => {
					expect(error.status).toBe(404);
					done();
				}
			});

			const req = httpMock.expectOne('api/user/999');
			expect(req.request.method).toBe('DELETE');
			req.flush({ message: 'User not found' }, { status: 404, statusText: 'Not Found' });
		});
	});
});
