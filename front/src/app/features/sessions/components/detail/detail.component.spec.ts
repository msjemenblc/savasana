import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { TeacherService } from 'src/app/services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from 'src/app/services/session.service';

import { DetailComponent } from './detail.component';

describe('DetailComponent', () => {
    let component: DetailComponent;
    let fixture: ComponentFixture<DetailComponent>;
    let teacherService: TeacherService;
    let yogaService: SessionApiService;

    const mockSessionService = {
        sessionInformation: {
            admin: false,
            id: 1
        }
    }

    const mockYoga = {
        id: 1,
        name: 'Test Yoga',
        description: 'Yoga Session',
        date: new Date,
        teacher_id: 1,
        users: [1, 2, 3],
        createdAt: new Date,
        updatedAt: new Date
    }

    const mockTeacher = {
        id: 1,
        lastName: 'Doe',
        firstName: 'John',
        createdAt: new Date,
        updatedAt: new Date
    }

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [DetailComponent], 
            imports: [
                RouterTestingModule,
                HttpClientModule,
                MatCardModule,
                MatSnackBarModule,
                MatIconModule,
                ReactiveFormsModule
            ],
            providers: [
                TeacherService,
                SessionApiService,
                {
                    provide: SessionService,
                    useValue: mockSessionService
                }
            ],
        }).compileComponents();

        fixture = TestBed.createComponent(DetailComponent);
        component = fixture.componentInstance;
        teacherService = TestBed.inject(TeacherService);
        yogaService = TestBed.inject(SessionApiService);
        fixture.detectChanges();
    });

    describe('Unit Test Suites', () => {
        it('should create', () => {
            expect(component).toBeTruthy();
        });
    });

    describe('Integration Test Suites', () => {
        beforeEach(() => {
            jest.spyOn(yogaService, 'detail').mockReturnValue(of(mockYoga));
            jest.spyOn(teacherService, 'detail').mockReturnValue(of(mockTeacher));

            component.ngOnInit();
            fixture.detectChanges();
        })

        it('should display session details when fetched successfully', () => {
            const title = fixture.nativeElement.querySelector('h1').textContent;
            expect(title).toContain(mockYoga.name);

            const spans = fixture.nativeElement.querySelectorAll('.ml1');
            const attendees = spans[2].textContent;
            expect(attendees).toContain(mockYoga.users.length + " attendees");
        });

        it('should display teacher details when fetched successfully', () => {
            const spans = fixture.nativeElement.querySelectorAll('.ml1');
            const teacher = spans[1].textContent;
            expect(teacher).toContain(`${mockTeacher.firstName} ${mockTeacher.lastName.toUpperCase()}`)
        });

        it('should show the participate toggle button', () => {
            component.isParticipate = false;
            fixture.detectChanges();

            const buttons = fixture.nativeElement.querySelectorAll('button');
            const participateButton = buttons[1].textContent;
            expect(participateButton).toContain('Participate');
        });

        it('should show the unparticipate toggle button', () => {
            component.isParticipate = true;
            fixture.detectChanges();

            const buttons = fixture.nativeElement.querySelectorAll('button');
            const unParticipateButton = buttons[1].textContent;
            expect(unParticipateButton).toContain('Do not participate');
        });
    });
});

