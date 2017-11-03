package senha

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EmailController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Email.list(params), model:[emailCount: Email.count()]
    }

    def show(Email email) {
        respond email
    }

    def create() {
        respond new Email(params)
    }

    @Transactional
    def save(Email email) {
        if (email == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (email.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond email.errors, view:'create'
            return
        }

        email.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'email.label', default: 'Email'), email.id])
                redirect email
            }
            '*' { respond email, [status: CREATED] }
        }
    }

    def edit(Email email) {
        respond email
    }

    @Transactional
    def update(Email email) {
        if (email == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (email.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond email.errors, view:'edit'
            return
        }

        email.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'email.label', default: 'Email'), email.id])
                redirect email
            }
            '*'{ respond email, [status: OK] }
        }
    }

    @Transactional
    def delete(Email email) {

        if (email == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        email.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'email.label', default: 'Email'), email.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'email.label', default: 'Email'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
