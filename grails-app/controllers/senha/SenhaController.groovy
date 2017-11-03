package senha

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SenhaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Senha.list(params), model:[senhaCount: Senha.count()]
    }

    def show(Senha senha) {
        respond senha
    }


    @Transactional
    def save(Senha senha) {
        if (senha == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (senha.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond senha.errors, view:'create'
            return
        }

        senha.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'senha.label', default: 'Senha'), senha.id])
                redirect senha
            }
            '*' { respond senha, [status: CREATED] }
        }
    }

    def edit(Senha senha) {
        respond senha
    }

    @Transactional
    def update(Senha senha) {
        if (senha == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (senha.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond senha.errors, view:'edit'
            return
        }

        senha.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'senha.label', default: 'Senha'), senha.id])
                redirect senha
            }
            '*'{ respond senha, [status: OK] }
        }
    }

    @Transactional
    def delete(Senha senha) {

        if (senha == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        senha.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'senha.label', default: 'Senha'), senha.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'senha.label', default: 'Senha'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
