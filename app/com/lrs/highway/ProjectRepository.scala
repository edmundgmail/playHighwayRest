package com.lrs.highway

import javax.inject.Inject

import com.google.inject.Singleton
import com.lrs.daos.core.{DocumentDao, Repository}
import com.lrs.models.Project
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.IndexType

import scala.concurrent.Future

@Singleton
class ProjectRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) extends DocumentDao[Project](reactiveMongoApi) with Repository[Project]  {

  override def collectionName = "ProjectTable"

  override def ensureIndexes: Future[Boolean] = ensureIndex(List("projectName" -> IndexType.Ascending), unique = true)

  ensureIndexes
}
