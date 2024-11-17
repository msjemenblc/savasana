import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
	let service: SessionApiService;
	let httpMock: HttpTestingController;

	const mockSessions: Session[] = [
		{ 
			id: 1, 
			name: 'FirstTest', 
			description: 'DescTest', 
			date: new Date, 
			teacher_id: 1, 
			users: [], 
			createdAt: new Date, 
			updatedAt: new Date 
		},
		{ 
			id: 2, 
			name: 'SecondTest', 
			description: 'DescTest', 
			date: new Date, 
			teacher_id: 1, 
			users: [], 
			createdAt: new Date, 
			updatedAt: new Date 
		}
	];

	beforeEach(() => {
		TestBed.configureTestingModule({
			imports:[
				HttpClientTestingModule
			],
			providers: [
				SessionApiService
			]
		});

		service = TestBed.inject(SessionApiService);
		httpMock = TestBed.inject(HttpTestingController);
	});

	afterEach(() => {
		httpMock.verify();
	});

	describe('Unit Test Suites', () => {
		it('should be created', () => {
			expect(service).toBeTruthy();
		});

		it('should fetch all sessions', () => {
			service.all().subscribe((sessions) => {
				expect(sessions).toEqual(mockSessions);
			});

			const req = httpMock.expectOne('api/session');
			expect(req.request.method).toBe('GET');
			req.flush(mockSessions);
		});

		it('should fetch session details by ID', () => {
			service.detail('1').subscribe((session) => {
				expect(session).toEqual(mockSessions[0]);
			});

			const req = httpMock.expectOne('api/session/1');
			expect(req.request.method).toBe('GET');
			req.flush(mockSessions[0]);
		});

		it('should handle 404 error when session not found', (done) => {
			service.detail('999').subscribe({
				next: () => fail('Expected an error, not a session'),
				error: (error) => {
					expect(error.status).toBe(404);
					done();
				}
			});

			const req = httpMock.expectOne('api/session/999');
			expect(req.request.method).toBe('GET');
			req.flush({ message: 'User not found' }, { status: 404, statusText: 'Not Found' });
		});

		it('should delete a session by ID', () => {
			service.delete('1').subscribe((response) => {
				expect(response).toEqual({ message:	'User deleted' });
			});

			const req = httpMock.expectOne('api/session/1');
			expect(req.request.method).toBe('DELETE');
			req.flush({ message: 'Session deleted' });
		});

		it('should handle 404 error when trying to delete a non-existent session', (done) => {
			service.delete('999').subscribe({
				next: () => fail('Expected an error, not a success response'),
				error: (error) => {
					expect(error.status).toBe(404);
					done();
				}
			});

			const req = httpMock.expectOne('api/session/999');
			expect(req.request.method).toBe('DELETE');
			req.flush({ message: 'Session not found' }, { status: 404, statusText: 'Not Found' });
		});

		it('should create a session', () => {
			const newSession: Session = { 
				id: 3, 
				name: 'CreatedTest', 
				description: 'DescTest', 
				date: new Date, 
				teacher_id: 1, 
				users: [], 
				createdAt: new Date, 
				updatedAt: new Date 
			};

			service.create(newSession).subscribe(session => {
				expect(session).toEqual(newSession);
			});

			const req = httpMock.expectOne('api/session');
			expect(req.request.method).toBe('POST');
			expect(req.request.body).toEqual(newSession);

			req.flush(newSession);
		});

		it('should update a session', () => {
			const updatedSession: Session = {
				id: 1, 
				name: 'UpdatedTest', 
				description: 'DescTest',
				date: new Date, 
				teacher_id: 1, 
				users: [], 
				createdAt: new Date, 
				updatedAt: new Date 
			};

			service.update('1', updatedSession).subscribe(session => {
				expect(session).toEqual(updatedSession);
			});

			const req = httpMock.expectOne('api/session/1');
			expect(req.request.method).toBe('PUT');
			expect(req.request.body).toEqual(updatedSession);

			req.flush(updatedSession);
		});

		it('should participate in a session', () => {
			const sessionId = '1';
			const userId = '5';
	
			service.participate(sessionId, userId).subscribe(() => {
				expect(true).toBeTruthy(); // On s'assure juste qu'il n'y a pas d'erreurs
			});
	
			const req = httpMock.expectOne('api/session/1/participate/5');
			expect(req.request.method).toBe('POST');
			expect(req.request.body).toBeNull();
	
			req.flush(null);
		});

		it('should unparticipate from a session', () => {
			const sessionId = '1';
			const userId = '5';

			service.unParticipate(sessionId, userId).subscribe(() => {
				expect(true).toBeTruthy(); // On s'assure juste qu'il n'y a pas d'erreurs
			});

			const req = httpMock.expectOne('api/session/1/participate/5');
			expect(req.request.method).toBe('DELETE');
			expect(req.request.body).toBeNull();

			req.flush(null);
		});
	});
});
