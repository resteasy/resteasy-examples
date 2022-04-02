let baseURI = 'http://localhost:9095';

Envjs(baseURI + '/test.html', {
    scriptTypes: {
        "text/javascript": true
    },
    logLevel: Envjs.DEBUG
});

REST.loglevel = 1;

function testFunctions() {
    assertNotNull("get function", MyResource.get);
}

function testGet() {
    let data = MyResource.get();
    assertEquals("ok", data);
}

function testGetFoo() {
    let data = MyResource.getFoo();
    assertEquals("foo", data);
}

function testGetParam() {
    let value = "bla";
    let data = MyResource.getParam({param: value});
    assertEquals(value, data);
}

/*
function testGetFooParam() {
	let data = MyResource.getFooParam({param: "paramv", other: "otherv",
		q : "qv", c : "cv", h: "hv", m: "mv"});
	// cookie is null since env.js doesn't support cookies yet :(
	assertEquals("param=paramv;other=otherv;q=qv;c=null;h=hv;m=mv;", data);
}

function testPutFooParam() {
	let data = MyResource.putFooParam({param: "paramv", other: "otherv",
		q : "qv", c : "cv", h: "hv", m: "mv", $entity: "entityv"});
	// cookie is null since env.js doesn't support cookies yet :(
	assertEquals("param=paramv;other=otherv;q=qv;c=null;h=hv;m=mv;entity=entityv;", data);
}
*/
function testGetXML() {
    let data = MyResource.getXML();
    print(data);
    if (data instanceof Document) {
        assertTrue(data instanceof Document);
        let root = data.documentElement;
    } else {
        let parser = new DOMParser()
        let root = parser.parseFromString(data, "application/xml").childNodes[1];
    }
    assertEquals("test", root.nodeName);
    assertEquals(1, root.childNodes.length);
    assertEquals("var", root.childNodes[0].nodeName);
    assertEquals(1, root.childNodes[0].childNodes.length);
    assertEquals("foo", root.childNodes[0].childNodes[0].nodeValue);
}

function testGetJSON() {
    let data = MyResource.getJSON();
    assertEquals(data['var'], "foo");
}

function testGetJSONStarMIME() {
    let data = MyResource.getJSONStarMIME();
    assertEquals(data['var'], "foo");
}

/*
function testPutJSON() {
	let toSend = {'var' : "ok"};
	let data = MyResource.putJSON({$entity: toSend});
	assertEquals("ok", data);
}
*/
function testPutXML() {
    let toSend = document.implementation.createDocument(null, "test", null);
    let v = toSend.createElement("var");
    toSend.documentElement.appendChild(v);
    v.appendChild(toSend.createTextNode("ok"));
    let data = MyResource.putXML({$entity: toSend});
    assertEquals("ok", data);
}

function testGetMultiRepresentationXML() {
    let data = MyResource.getMultiRepresentation({$accepts: "application/xml"});
    if (data instanceof Document) {
        assertTrue(data instanceof Document);
        let root = data.documentElement;
    } else {
        let parser = new DOMParser()
        let root = parser.parseFromString(data, "application/xml").childNodes[1];
    }
    assertEquals(23, root.childNodes.length);
}

function testGetMultiRepresentationJSON() {
    let data = MyResource.getMultiRepresentation({$accepts: "application/json"});
    assertTrue(data instanceof Array);
    assertEquals(23, data.length);
}

function testLookup() {
    let value = "foo";
    let data = MyResource.lookup({id: value});
    assertEquals(baseURI + "/rest/mine/" + value, data);
}

function testSubResource() {
    let data = MyResource.getSubResource.get();
    assertEquals("Hello", data);
}

function testSubResourceWithPath() {
    let data = MyResource.getSubResource.getWithPath();
    assertEquals("Hello withPath", data);
}

/*

function testSubResource2() {
	let data = MyResource.getSubResource2.get({id: "a", foo: "b"});
	assertEquals("Hello a/b", data);
}

function testSubResource2WithPath() {
	let data = MyResource.getSubResource2.getWithPath({id: "a", foo: "b", bar: "c"});
	assertEquals("Hello withPath a/b/c", data);
}
*/
function testDoubleSubResource() {
    let data = MyResource.getSubResource2.getSubResource.get({id: "a"});
    assertEquals("Hello", data);
}

function testDoubleSubResourceWithPath() {
    let data = MyResource.getSubResource2.getSubResource.getWithPath({id: "a"});
    assertEquals("Hello withPath", data);
}

function testForm() {
    let data = MyResource.postForm({a: "aa", b: "bb"});
    assertEquals("aa/bb", data);
}

// Encoding

function testUTF8() {
    assertEquals("%61", REST.Encoding.percentUTF8(0x61));
    assertEquals("%ce%91", REST.Encoding.percentUTF8(0x0391));
    assertEquals("%e2%89%a2", REST.Encoding.percentUTF8(0x2262));
    assertEquals("%f0%a3%8e%b4", REST.Encoding.percentUTF8(0x233B4));
}

function testPercentByte() {
    assertEquals("%05", REST.Encoding.percentByte(5));
    assertEquals("%20", REST.Encoding.percentByte(32));
}

function testEncoder() {
    assertEquals("abc", REST.Encoding.encodeFormNameOrValue("abc"));
    assertEquals("%c3%a9", REST.Encoding.encodeFormNameOrValue("é"));
    assertEquals("%f0%9f%82%84", REST.Encoding.encodeFormNameOrValue("🂄"));
}

function testEncoders() {
    assertEquals("abc%24%2d%5f%2e%2b%21%2a%27%28%29%2c%2f%3f%26%3d%23+%0D%0A", REST.Encoding.encodeFormNameOrValue("abc$-_.+!*'(),/?&=# \n"));
    assertEquals("azAZ09-._~!$&'()*+,%3b=:@%c3%a9%2f%3f%23%5b%5d", REST.Encoding.encodePathParamValue("azAZ09-._~!$&'()*+,;=:@é/?#[]"));
    assertEquals("azAZ09-._~!$&'()*+,%3b=:@%c3%a9%2f%3f%23%5b%5d", REST.Encoding.encodePathSegment("azAZ09-._~!$&'()*+,;=:@é/?#[]"));
    assertEquals("azAZ09-._~!$&'()*+,%3b%3d:@%c3%a9%2f%3f%23%5b%5d", REST.Encoding.encodePathParamName("azAZ09-._~!$&'()*+,;=:@é/?#[]"));
    assertEquals("azAZ09-._~!$%26'()*%2b,;%3d:@%c3%a9/?%23%5b%5d", REST.Encoding.encodeQueryParamNameOrValue("azAZ09-._~!$&'()*+,;=:@é/?#[]"));
}
