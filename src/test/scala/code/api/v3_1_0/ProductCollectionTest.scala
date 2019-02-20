/**
Open Bank Project - API
Copyright (C) 2011-2018, TESOBE Ltd

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Email: contact@tesobe.com
TESOBE Ltd
Osloerstrasse 16/17
Berlin 13359, Germany

This product includes software developed at
TESOBE (http://www.tesobe.com/)
  */
package code.api.v3_1_0

import code.api.ResourceDocs1_4_0.SwaggerDefinitionsJSON
import code.api.util.APIUtil.OAuth._
import code.api.util.ApiRole.CanMaintainProductCollection
import code.api.util.ApiVersion
import code.api.v3_1_0.OBPAPI3_1_0.Implementations3_1_0
import code.entitlement.Entitlement
import com.github.dwickern.macros.NameOf.nameOf
import net.liftweb.json.Serialization.write
import net.liftweb.json.prettyRender
import org.scalatest.Tag

class ProductCollectionTest extends V310ServerSetup {

  override def beforeAll(): Unit = {
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    super.afterAll()
  }

  /**
    * Test tags
    * Example: To run tests with tag "getPermissions":
    * 	mvn test -D tagsToInclude
    *
    *  This is made possible by the scalatest maven plugin
    */
  object VersionOfApi extends Tag(ApiVersion.v3_1_0.toString)
  object ApiEndpoint1 extends Tag(nameOf(Implementations3_1_0.createProductCollections))

  lazy val testBankId = randomBankId
  lazy val putProductCollectionsV310 = SwaggerDefinitionsJSON.putProductCollectionsV310

  feature("Create Product Collections v3.1.0") {
    scenario("We will call the endpoint with a user credentials", ApiEndpoint1, VersionOfApi) {
      Entitlement.entitlement.vend.addEntitlement(testBankId, resourceUser1.userId, CanMaintainProductCollection.toString)
      When("We make a request v3.1.0")
      val request310 = (v3_1_0_Request / "banks" / testBankId / "product-collections" / "A portfolio of car loans is a ABS").PUT <@ (user1)
      val response310 = makePutRequest(request310, write(putProductCollectionsV310))
      Then("We should get a 201")
      response310.code should equal(201)
      response310.body.extract[ProductCollectionsJsonV310]
      org.scalameta.logger.elem(prettyRender(response310.body))
    }
  }

}
