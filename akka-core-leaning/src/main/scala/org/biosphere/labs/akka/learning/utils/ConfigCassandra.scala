package org.biosphere.labs.akka.learning.utils

import com.datastax.driver.core.{Cluster}
import com.typesafe.config.ConfigFactory

object Keyspaces {
  private val config = ConfigFactory.load()
  private val akkaCassandraConfig = config.getConfig("akka-cassandra.main.db.cassandra")
  val akkaCassandra = akkaCassandraConfig.getString("keyspace")
}

trait CassandraCluster {
  def cluster: Cluster
}

trait ConfigCassandraCluster extends CassandraCluster {
  private val config = ConfigFactory.load()
  private val cassandraConfig = config.getConfig("akka-cassandra.main.db.cassandra")
  private val port = cassandraConfig.getInt("port")
  //private val hosts = cassandraConfig.getStringList("hosts").toArray

  lazy val cluster: Cluster =
    Cluster.builder().
      //TODO get the list of hosts from the configuration (List of)
      //addContactPoints(hosts).
      addContactPoints("172.17.0.2").
      //TODO find and add as dependency the compression jar...
      //withCompression(ProtocolOptions.Compression.SNAPPY).
      withPort(port).
      build()
}