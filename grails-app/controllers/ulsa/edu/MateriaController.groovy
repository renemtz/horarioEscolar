package ulsa.edu



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class MateriaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Materia.list(params), model:[materiaInstanceCount: Materia.count()]
    }

    def show(Materia materiaInstance) {
        respond materiaInstance
    }

    def create() {
        respond new Materia(params)
    }

    @Transactional
    def save(Materia materiaInstance) {
        if (materiaInstance == null) {
            notFound()
            return
        }

        if (materiaInstance.hasErrors()) {
            respond materiaInstance.errors, view:'create'
            return
        }

        materiaInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'materiaInstance.label', default: 'Materia'), materiaInstance.id])
                redirect materiaInstance
            }
            '*' { respond materiaInstance, [status: CREATED] }
        }
    }

    def edit(Materia materiaInstance) {
        respond materiaInstance
    }

    @Transactional
    def update(Materia materiaInstance) {
        if (materiaInstance == null) {
            notFound()
            return
        }

        if (materiaInstance.hasErrors()) {
            respond materiaInstance.errors, view:'edit'
            return
        }

        materiaInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Materia.label', default: 'Materia'), materiaInstance.id])
                redirect materiaInstance
            }
            '*'{ respond materiaInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Materia materiaInstance) {

        if (materiaInstance == null) {
            notFound()
            return
        }

        materiaInstance.delete flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Materia.label', default: 'Materia'), materiaInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'materiaInstance.label', default: 'Materia'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
