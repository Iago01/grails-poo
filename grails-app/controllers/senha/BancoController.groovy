package senha

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BancoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Banco.list(params), model:[bancoCount: Banco.count()]
    }

    def show(Banco banco) {
        respond banco
    }

    def create() {
        respond new Banco(params)
    }

    @Transactional
    def save(Banco banco) {
        if (banco == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (banco.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond banco.errors, view:'create'
            return
        }

        banco.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'banco.label', default: 'Banco'), banco.id])
                redirect banco
            }
            '*' { respond banco, [status: CREATED] }
        }
    }

    def edit(Banco banco) {
        respond banco
    }

    @Transactional
    def update(Banco banco) {
        if (banco == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (banco.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond banco.errors, view:'edit'
            return
        }

        banco.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'banco.label', default: 'Banco'), banco.id])
                redirect banco
            }
            '*'{ respond banco, [status: OK] }
        }
    }

    @Transactional
    def delete(Banco banco) {

        if (banco == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        banco.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'banco.label', default: 'Banco'), banco.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'banco.label', default: 'Banco'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
