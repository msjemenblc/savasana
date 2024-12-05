/// <reference types="cypress" />

describe('Login spec', () => {
	it('should visit sessions page when login is successful', () => {
		cy.visit('/login')

		cy.intercept('POST', '/api/auth/login', {
			body: {
				id: 1,
				username: 'userName',
				firstName: 'firstName',
				lastName: 'lastName',
				admin: true
			},
		})

		cy.intercept(
		{
			method: 'GET',
			url: '/api/session',
		},
		[]).as('session')

		cy.get('input[formControlName=email]').type("yoga@studio.com")
		cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

		cy.url().should('include', '/sessions')
	})

	it('should show error message when login fails', () => {
		cy.visit('/login')

		cy.intercept('POST', '/api/auth/login', {
			statusCode: 401,
			body: { message: 'Bad credentials' }
		})

		cy.get('input[formControlName=email]').type("fail@studio.com")
		cy.get('input[formControlName=password]').type(`${"fail!1234"}{enter}{enter}`)

		cy.url().should('not.include', '/sessions')

		cy.get('p.error').should('contain', 'An error occurred')
	})
});