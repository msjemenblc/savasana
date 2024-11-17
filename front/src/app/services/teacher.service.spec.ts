import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
	let service: TeacherService;
	let httpMock: HttpTestingController;

	beforeEach(() => {
		TestBed.configureTestingModule({
			imports: [
				HttpClientTestingModule
			],
			providers: [
				TeacherService
			]
		});

		service = TestBed.inject(TeacherService);
		httpMock = TestBed.inject(HttpTestingController);
	});

	afterEach(() => {
		httpMock.verify();
	});

	describe('Unit Test Suites', () => {
		it('should be created', () => {
			expect(service).toBeTruthy();
		});

		it('should fetch all teachers', () => {
			const mockTeachers: Teacher[] = [
				{ id: 1, firstName: 'FirstTestOne', lastName: 'LastTestOne', createdAt: new Date, updatedAt: new Date },
				{ id: 2, firstName: 'FirstTestTwo', lastName: 'LastTestTwo', createdAt: new Date, updatedAt: new Date },
			];

			service.all().subscribe((teachers) => {
				expect(teachers.length).toBe(2);
				expect(teachers).toEqual(mockTeachers);
			});

			const req = httpMock.expectOne('api/teacher');
			expect(req.request.method).toBe('GET');
			req.flush(mockTeachers);
		});

		it('should fetch details of a teacher by ID', () => {
			const mockTeacher: Teacher = { id: 1, firstName: 'FirstTest', lastName: 'LastTest', createdAt: new Date, updatedAt: new Date };

			service.detail('1').subscribe((teacher) => {
				expect(teacher).toEqual(mockTeacher);
			});

			const req = httpMock.expectOne('api/teacher/1');
			expect(req.request.method).toBe('GET');
			req.flush(mockTeacher);
		});
	});
});
