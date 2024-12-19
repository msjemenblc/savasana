/// <reference types="cypress" />

describe('Session spec', () => {
    beforeEach(() => {
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

        cy.intercept('GET', '/api/session', {
            statusCode: 200,
            body: [
                {
                    id: 1,
                    name: "Yoga Session 1",
                    date: "2025-01-01T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "Relaxing yoga session !",
                    users: [12, 18, 21],
                    createdAt: "2024-11-10T16:30:06",
                    updatedAt: "2024-11-10T16:30:06",
                },
                {
                    id: 2,
                    name: "Yoga Session 2",
                    date: "2025-01-13T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "YOGA !",
                    users: [12, 13, 20, 49],
                    createdAt: "2024-11-18T16:30:06",
                    updatedAt: "2024-11-18T16:30:06",
                }
            ]
        }).as('sessions')

        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
                id: 1,
                name: "Yoga Session 1",
                date: "2025-01-01T00:00:00.000+00:00",
                teacher_id: 1,
                description: "Relaxing yoga session !",
                users: [12, 18, 21],
                createdAt: "2024-11-10T16:30:06",
                updatedAt: "2024-11-10T16:30:06", 
            }
        }).as('sessionDetail')

        cy.get('input[formControlName=email]').type("yoga@studio.com")
		cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    })

    it('should display list session with correct data', () => {
        cy.get('.items').should('exist')
        cy.get('.items .item').should('have.length', 2)

        cy.get('.items .item').eq(0).within(() => {
            cy.contains('Yoga Session 1')
            cy.contains('Session on January 1, 2025')
        })

        cy.get('.items .item').eq(1).within(() => {
            cy.contains('Yoga Session 2')
            cy.contains('Session on January 13, 2025')
        })
    })

    it('should navigate to details and display with correct data', () => {
        cy.get('.items .item').should('exist')

        cy.get('.items .item').first().within(() => {
            cy.contains('button', 'Detail').click()
        })

        cy.url().should('include', '/detail/1')

        cy.get('h1').should('contain', 'Yoga Session 1')
        cy.get('.ml1').eq(1).should('contain', '3 attendees')
        cy.get('.ml1').eq(2).should('contain', 'January 1, 2025')
        cy.get('.description').should('contain', 'Relaxing yoga session !')
        cy.get('.created').should('contain', 'November 10, 2024')
        cy.get('.updated').should('contain', 'November 10, 2024')
    })

    it('should create a session and display it in the list', () => {
        cy.intercept('POST', '/api/session', {
            statusCode: 201,
            body: {
                id: 3,
                name: "New Yoga Session",
                date: "2025-02-20T00:00:00.000+00:00",
                teacher_id: 1,
                description: "A brand new relaxing yoga session",
                users: [],
                createdAt: "2024-12-02T16:30:06",
                updatedAt: "2024-12-02T16:30:06",
            }
        }).as('createSession')

        cy.intercept('GET', '/api/teacher', {
            statusCode: 200,
            body: [
                {
                    "id": 1,
                    "lastName": "DELAHAYE",
                    "firstName": "Margot",
                    "createdAt": "2024-11-10T14:30:15",
                    "updatedAt": "2024-11-10T14:30:15"
                },
                {
                    "id": 2,
                    "lastName": "THIERCELIN",
                    "firstName": "Hélène",
                    "createdAt": "2024-11-10T14:30:15",
                    "updatedAt": "2024-11-10T14:30:15"
                }
            ]
        }).as('loadTeachers')
    
        cy.contains('button', 'Create').click()
    
        cy.get('input[formControlName=name]').type('New Yoga Session')
        cy.get('input[formControlName=date]').type('2025-02-20')
        cy.get('mat-select[formControlName=teacher_id]').click()
        cy.contains('mat-option', 'Hélène THIERCELIN').click()
        cy.get('textarea[formControlName=description]').type('A brand new relaxing yoga session')
    
        // Soumettre le formulaire
        cy.contains('button', 'Save').click()
    
        cy.get('.mat-snack-bar-container')
        .should('exist') 
        .and('contain.text', 'Session created !')
    })

    it('should delete the first session', () => {
        cy.intercept('DELETE', '/api/session/1', {
            statusCode: 200
        }).as('deleteSession')

        cy.get('.items .item').should('exist')

        cy.get('.items .item').first().within(() => {
            cy.contains('button', 'Detail').click()
        })

        cy.get('.ml1').eq(0).should('contain', 'Delete').click()

        cy.get('.mat-snack-bar-container')
        .should('exist')
        .and('contain.text', 'Session deleted !')
    })
    
})