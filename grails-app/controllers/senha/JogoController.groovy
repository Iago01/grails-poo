package senha

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class JogoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Jogo.list(params), model:[jogoCount: Jogo.count()]
    }

    def show(Jogo jogo) {
        respond jogo
    }

    def create() {
        respond new Jogo(params)
    }

    @Transactional
    def save(Jogo jogo) {
        if (jogo == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (jogo.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond jogo.errors, view:'create'
            return
        }

        jogo.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'jogo.label', default: 'Jogo'), jogo.id])
                redirect jogo
            }
            '*' { respond jogo, [status: CREATED] }
        }
    }

    def edit(Jogo jogo) {
        respond jogo
    }

    @Transactional
    def update(Jogo jogo) {
        if (jogo == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (jogo.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond jogo.errors, view:'edit'
            return
        }

        jogo.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'jogo.label', default: 'Jogo'), jogo.id])
                redirect jogo
            }
            '*'{ respond jogo, [status: OK] }
        }
    }

    @Transactional
    def delete(Jogo jogo) {

        if (jogo == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        jogo.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'jogo.label', default: 'Jogo'), jogo.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'jogo.label', default: 'Jogo'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
