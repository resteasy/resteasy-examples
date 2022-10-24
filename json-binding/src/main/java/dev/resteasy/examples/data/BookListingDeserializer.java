/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2022 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.resteasy.examples.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.stream.JsonParser;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class BookListingDeserializer implements JsonbDeserializer<BookListing> {
    @Override
    public BookListing deserialize(final JsonParser parser, final DeserializationContext ctx, final Type rtType) {
        final List<Book> books = new ArrayList<>();
        while (parser.hasNext()) {
            final JsonParser.Event event = parser.next();
            if (event == JsonParser.Event.START_OBJECT) {
                books.add(ctx.deserialize(Book.class, parser));
            }
            if (event == JsonParser.Event.END_OBJECT) {
                break;
            }
        }
        return new BookListing(books);
    }
}
