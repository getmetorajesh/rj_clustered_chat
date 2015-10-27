package controllers

import javax.inject._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api._
import play.api.Play._
import actors.{UserSocket, ChatRoom}
import play.api.mvc.{Action, Controller, WebSocket}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.JsValue
import play.api.Play.current
import akka.actor.ActorSystem
import scala.concurrent.Future
import akka.actor._

@Singleton
class Chat @Inject()(val messagesApi: MessagesApi, system:ActorSystem) extends Controller with I18nSupport {

  val User = "user"
  var chatRoom = system.actorOf(Props[ChatRoom], "chat-room")
  val signInForm = Form(single("name" -> nonEmptyText))
  
  def index = Action { implicit request =>
       Ok(views.html.loginView(signInForm))
  }
  
  def chat = Action { implicit request => 
    request.session.get(User).map { user =>
      Ok(views.html.chat(user))  
    }.getOrElse(Redirect(routes.Chat.index()))
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
  
  def signOut = Action { implicit request =>
      Redirect(routes.Chat.index).withNewSession;
  }
  
}