package controllers;

import datamodel.LocalMusic;

public class NewCommentController implements Controller{

  private LocalMusic localMusic;
  
  private CommentsController commentsController;
  
  
  public CommentsController getCommentsController() {
    return commentsController;
  }

  public void setCommentsController(CommentsController commentsController) {
    this.commentsController = commentsController;
  }
  
  public void init(LocalMusic localMusic) {
    this.localMusic = localMusic;
  }
  
  @Override
  public void initialize() {
        
  }


  
}
