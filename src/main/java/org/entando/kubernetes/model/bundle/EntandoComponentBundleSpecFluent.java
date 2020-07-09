/*
 *
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 */

package org.entando.kubernetes.model.bundle;

import io.fabric8.kubernetes.api.builder.Nested;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EntandoComponentBundleSpecFluent<A extends EntandoComponentBundleSpecFluent> {

    protected EntandoComponentBundleDetailsBuilder details;
    protected List<EntandoComponentBundleTagBuilder> tags;

    public EntandoComponentBundleSpecFluent(EntandoComponentBundleSpec spec) {
        this.details = new EntandoComponentBundleDetailsBuilder(spec.getDetails());
        this.tags = createTagBuilders(spec.getTags());
    }

    public EntandoComponentBundleSpecFluent() {
        this.details = new EntandoComponentBundleDetailsBuilder();
        this.tags = new ArrayList<>();
    }

    private List<EntandoComponentBundleTagBuilder> createTagBuilders(List<EntandoComponentBundleTag> tags) {
        return new ArrayList<>(tags.stream().map(EntandoComponentBundleTagBuilder::new).collect(Collectors.toList()));
    }

    public DetailsNested<A> withNewDetails() {
        return new DetailsNested<>(thisAsA());
    }

    public DetailsNested<A> editDetails() {
        return new DetailsNested<>(thisAsA(), details.build());
    }

    public TagNested<A> addNewTag() {
        return new TagNested<>(thisAsA());
    }

    public A addToTags(EntandoComponentBundleTag tag) {
        this.tags.add(new EntandoComponentBundleTagBuilder(tag));
        return thisAsA();
    }

    @SuppressWarnings("unchecked")
    protected A thisAsA() {
        return (A) this;
    }

    public A withDetails(EntandoComponentBundleDetails details) {
        this.details = new EntandoComponentBundleDetailsBuilder(details);
        return thisAsA();
    }

    public EntandoComponentBundleSpec build() {
        return new EntandoComponentBundleSpec(this.details.build(),
                this.tags.stream().map(EntandoComponentBundleTagFluent::build).collect(Collectors.toList()));
    }

    public A withTags(List<EntandoComponentBundleTag> tags) {
        this.tags = createTagBuilders(tags);
        return thisAsA();
    }

    public static class DetailsNested<N extends EntandoComponentBundleSpecFluent> extends
            EntandoComponentBundleDetailsFluent<DetailsNested<N>> implements Nested<N> {

        private final N parentBuilder;

        public DetailsNested(N parentBuilder, EntandoComponentBundleDetails details) {
            super(details);
            this.parentBuilder = parentBuilder;
        }

        public DetailsNested(N parentBuilder) {
            super();
            this.parentBuilder = parentBuilder;
        }

        @SuppressWarnings("unchecked")
        public N and() {
            return (N) parentBuilder.withDetails(super.build());
        }

        public N endDetails() {
            return and();
        }
    }

    public static class TagNested<N extends EntandoComponentBundleSpecFluent> extends
            EntandoComponentBundleTagFluent<TagNested<N>> implements Nested<N> {

        private final N parentBuilder;

        public TagNested(N parentBuilder) {
            super();
            this.parentBuilder = parentBuilder;
        }

        @SuppressWarnings("unchecked")
        public N and() {
            return (N) parentBuilder.addToTags(super.build());
        }

        public N endTag() {
            return and();
        }
    }

}
