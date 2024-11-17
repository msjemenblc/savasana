import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { expect } from '@jest/globals';

import { NotFoundComponent } from './not-found.component';

describe('NotFoundComponent', () => {
	let component: NotFoundComponent;
	let fixture: ComponentFixture<NotFoundComponent>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [ NotFoundComponent ]
		}).compileComponents();

		fixture = TestBed.createComponent(NotFoundComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	describe('Unit Test Suites', () => {
		it('should create', () => {
			expect(component).toBeTruthy();
		});

		it('should display the correct message', () => {
			const element = fixture.debugElement.query(By.css('h1'));

			expect(element).toBeTruthy();
			expect(element.nativeElement.textContent.trim()).toBe('Page not found !');
		});
	});
});
