package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.validation._
import play.api.data.Forms._
//import play.api.data.validation
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

case class UserData(name:String, age:Int)

class Application extends Controller {
   
  val userForm = Form(
      
      mapping(
        "name" -> text, //use "nonEmptyText" constraint instead of plain text
        "age" -> number 
      )(UserData.apply)(UserData.unapply)
    )
    
  def index = Action {
   
    val userForm = Form(
      mapping(
        "name" -> text, //use "nonEmptyText" constraint instead of plain text
        //"name" -> text.verifying(nonEmpty), 
        "age" -> number 
       // "age" -> number.verifying(min(0), max(100)) // using validation pakag        
      )(UserData.apply)(UserData.unapply)
    )
    
   
    def validate(name:String, age:Int) = {
      name match{
        case "bob" if age >= 18 => 
          Some(UserData(name,age))
        case "admin" => 
          Some(UserData(name, age))
        case _ =>
          None
      }
    }
    
    val userFormConstraintsAdhoc = Form(
      mapping(
          "name"-> text, 
          "age"->number
          )(UserData.apply)(UserData.unapply) verifying("Failed form constraints!", 
              fields => fields match{
        case userData => validate(userData.name, userData.age).isDefined
      })
    )
    
    val userData = userForm.bind(Map("name"->"raj","age" ->"27")).get
    
    Ok(views.html.index("Your new application is ready."))
  }
  
  def index2 = Action { implicit request => 
     val userForm = Form(
      mapping(
        "name" -> text, //use "nonEmptyText" constraint instead of plain text
        "age" -> number 
      )(UserData.apply)(UserData.unapply)
    )
    
    userForm.bindFromRequest().fold(
        formWithErrors =>{
          //binding failure, you retrieve the form containing errors
          ///BadRequest(views.html.user(formWithErrors))
        },
        userData => {
         //   val newUser = models.User(userData.name, userData.age)
         //  val id = models.User.create(newUser)
          Redirect(routes.Application.index())
        }    
    )
    
    Ok("")
  }
  
  def user = Action {
    Ok(views.html.user(userForm))
  }
  
  /**
   * Using body parser to pares the form
   */
   val userPost = Action(parse.form(userForm)) { implicit request => 
     val userData = request.body
  //     val newUser = models.User(userData.name, userData.age)
  //   val id = models.User.create(newUser)
    // Redirect(routes.Application.index)
     Ok("")
   }
   
   /**
   * Using body parser to pares the form
   */
  val userPostWithErrors = Action(parse.form(userForm, 
    onErrors = (formWithErrors: Form[UserData]) => BadRequest(views.html.user(formWithErrors)))) 
    { implicit request =>
     val userData = request.body
//     val newUser = models.User(userData.name, userData.age)
  //   val id = models.User.create(newUser)
     Redirect(routes.Application.index)
   }

}
