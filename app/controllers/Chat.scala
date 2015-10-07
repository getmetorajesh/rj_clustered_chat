package controllers

import javax.inject._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api._
import play.api.Play._
import play.api.mvc.{Action, Controller, WebSocket}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.JsValue
import play.api.Play.current


@Singleton
class Chat @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val User = "user"
  val signInForm = Form(single("name" -> nonEmptyText))
  
  def index = Action { implicit request =>
       Ok(views.html.loginView(signInForm))
  }
  
  def chat = Action { implicit request => 
    
    Ok{"dsad"}
  }
  
  def loginPost = Action { implicit request =>
    signInForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.loginView(formWithErrors))
      },
      name => {
        Redirect(routes.Chat.chat())
          .withSession(request.session + (User -> name))
      }
    )
  }
  
}