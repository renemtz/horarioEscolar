package ulsa.edu



import grails.test.mixin.*
import spock.lang.*

@TestFor(MateriaController)
@Mock(Materia)
class MateriaControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.materiaInstanceList
            model.materiaInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.materiaInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            def materia = new Materia()
            materia.validate()
            controller.save(materia)

        then:"The create view is rendered again with the correct model"
            model.materiaInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            materia = new Materia(params)

            controller.save(materia)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/materia/show/1'
            controller.flash.message != null
            Materia.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def materia = new Materia(params)
            controller.show(materia)

        then:"A model is populated containing the domain instance"
            model.materiaInstance == materia
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def materia = new Materia(params)
            controller.edit(materia)

        then:"A model is populated containing the domain instance"
            model.materiaInstance == materia
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/materia/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def materia = new Materia()
            materia.validate()
            controller.update(materia)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.materiaInstance == materia

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            materia = new Materia(params).save(flush: true)
            controller.update(materia)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/materia/show/$materia.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/materia/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def materia = new Materia(params).save(flush: true)

        then:"It exists"
            Materia.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(materia)

        then:"The instance is deleted"
            Materia.count() == 0
            response.redirectedUrl == '/materia/index'
            flash.message != null
    }
}
