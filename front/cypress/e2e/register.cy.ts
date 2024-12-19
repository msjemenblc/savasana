/// <reference types="cypress" />

describe('Registration spec', () => {
    it('should register successfully and redirect to login page', () => {
        cy.visit('/register')

        cy.intercept('POST', '/api/auth/register', {
            statusCode: 200,
            body: { message: 'User registered successfully!' }
        })

        cy.get('input[formControlName=firstName]').type("John")
        cy.get('input[formControlName=lastName]').type("Doe")
        cy.get('input[formControlName=email]').type("testing@studio.com")
        cy.get('input[formControlName=password]').type("test!1234")
        cy.get('button[type=submit]').click()

        cy.url().should('include', '/login')
    })

    it('should show error when email is already taken', () => {
        cy.visit('/register')

        cy.intercept('POST', '/api/auth/register', {
            statusCode: 400,
            body: { message: 'Error: Email is already taken!' }
        })

        cy.get('input[formControlName=firstName]').type("Already")
        cy.get('input[formControlName=lastName]').type("Taken")
        cy.get('input[formControlName=email]').type("alreadytaken@studio.com")
        cy.get('input[formControlName=password]').type("taken!1234")
        cy.get('button[type=submit]').click()

        cy.url().should('not.include', '/login')

        cy.get('span.error').should('contain', 'An error occurred')
    })
})