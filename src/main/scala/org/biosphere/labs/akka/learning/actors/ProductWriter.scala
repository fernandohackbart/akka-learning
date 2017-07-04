package org.biosphere.labs.akka.learning.actors

import akka.actor.{Actor, ActorLogging}
import org.biosphere.labs.akka.learning.domain.OperationOutcome.FAIL
import org.biosphere.labs.akka.learning.domain.{Product, ProductOperationRequest, ProductOperationResponse}
import com.datastax.driver.core.{Cluster,Session,PreparedStatement}
import org.biosphere.labs.akka.learning.utils.Keyspaces



class ProductWriter(cluster: Cluster) extends Actor with ActorLogging {

  var session: Session = _
  var preparedStatement: PreparedStatement = _

  override def preStart = {
    log.debug("ProductWriter.preStart() Calling ensureKeyspace()")
    ensureKeyspace()
    log.debug("ProductWriter.preStart() Creating session")
    session = cluster.connect(Keyspaces.akkaCassandra)
    log.debug("ProductWriter.preStart() Calling ensureKeyspace()")
    createSchema()
    log.debug("ProductWriter.preStart() Creating  preparedStatement")
    preparedStatement = session.prepare("INSERT INTO product(id, brand, name) VALUES (?, ?, ?);")
  }

  def receive = {
    case ProductOperationRequest(operation, product) =>
      val s = sender
      log.info(s"ProductWriter operation ($operation) on ${product.brand}.${product.name}!")
      saveProduct(product)
      val por = ProductOperationResponse("OK")
      s ! por
    case _ =>
      log.warning("ProductWriter received unknown message")
      sender() ! FAIL
  }

  def saveProduct(product: Product): Unit = {
    val uuid = java.util.UUID.randomUUID.toString
    log.debug(s"ProductWriter.createSchema() Inserting product PK ($uuid)")
    session.executeAsync(preparedStatement.bind(uuid,product.brand, product.name))
    log.debug("ProductWriter.createSchema() Product saved")
  }

  def ensureKeyspace(): Unit = {
    log.debug("ProductWriter.ensureKeyspace() Connecting sessionInitial")
    val sessionInitial = cluster.connect()
    log.debug(s"ProductWriter.ensureKeyspace() Creating keyspace ${Keyspaces.akkaCassandra} over sessionInitial")
    sessionInitial.execute(s"CREATE KEYSPACE IF NOT EXISTS ${Keyspaces.akkaCassandra} WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};")
    log.debug("ProductWriter.ensureKeyspace() Closing sessionInitial")
    sessionInitial.close()
  }

  def createSchema(): Unit = {
    log.debug("ProductWriter.createSchema() Creating product table")
    //TODO replace the hardcoded AkkaLearning by the variable
    session.execute(
      """CREATE TABLE IF NOT EXISTS AkkaLearning.product (
        id text PRIMARY KEY,
        brand text,
        name text
        );""")
    log.debug("ProductWriter.createSchema() Table product  created")
  }

}
