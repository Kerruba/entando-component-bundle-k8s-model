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
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EntandoComponentBundleSpecFluent<A extends EntandoComponentBundleSpecFluent> {

    protected String code;
    protected String title;
    protected String description;
    protected EntandoComponentBundleAuthor author;
    protected String organization;
    protected String thumbnail;
    protected List<EntandoComponentBundleImage> images = new ArrayList<>();
    protected String url;
    protected List<EntandoComponentBundleVersionsBuilder> versions = new ArrayList<>();

    public EntandoComponentBundleSpecFluent(EntandoComponentBundleSpec spec) {
        this.code = spec.getCode();
        this.title = spec.getTitle();
        this.description = spec.getDescription();
        this.author = spec.getAuthor();
        this.organization = spec.getOrganization();
        this.thumbnail = spec.getThumbnail();
        this.images = spec.getImages();
        this.url = spec.getUrl();
        this.versions = createTagBuilders(spec.getVersions());
    }

    public EntandoComponentBundleSpecFluent() {
    }

    private List<EntandoComponentBundleVersionsBuilder> createTagBuilders(List<EntandoComponentBundleVersion> versions) {
        return versions.stream().map(EntandoComponentBundleVersionsBuilder::new).collect(Collectors.toList());
    }

    private List<EntandoComponentBundleImageBuilder> createImageBuilders(List<EntandoComponentBundleImage> images) {
        return images.stream().map(EntandoComponentBundleImageBuilder::new).collect(Collectors.toList());
    }

    public A withCode(String code) {
        this.code = code;
        return thisAsA();
    }

    public A withTitle(String title){
       this.title = title;
       return thisAsA();
    }

    public A withDescription(String description) {
        this.description = description;
        return thisAsA();
    }

    public A withAuthor(EntandoComponentBundleAuthor author) {
        this.author = author;
        return thisAsA();
    }
    
    public AuthorNested<A> withNewAuthor() {
        return new AuthorNested<>(thisAsA());
    }
    
    public A withOrganization(String organization){
       this.organization = organization;
       return thisAsA();
    }

    public A withThumbnail(String thumbnail){
       this.thumbnail = thumbnail;
       return thisAsA();
    }

    public A withUrl(String url){
       this.url = url;
       return thisAsA();
    }

    public A addToBundleImages(String imageUrl) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(new EntandoComponentBundleImage(imageUrl));
        return thisAsA();
    }

    public TagNested<A> addNewTag() {
        return new TagNested<>(thisAsA());
    }

    public A addToTags(EntandoComponentBundleVersion tag) {
        this.versions.add(new EntandoComponentBundleVersionsBuilder(tag));
        return thisAsA();
    }

    public A withTags(List<EntandoComponentBundleVersion> tags) {
        this.versions = createTagBuilders(tags);
        return thisAsA();
    }

    @SuppressWarnings("unchecked")
    protected A thisAsA() {
        return (A) this;
    }

    public EntandoComponentBundleSpec build() {
        return new EntandoComponentBundleSpec(
                this.code,
                this.title,
                this.description,
                this.author,
                this.organization,
                this.thumbnail,
                this.images,
                this.url,
                this.versions.stream().map(EntandoComponentBundleVersionFluent::build).collect(Collectors.toList()));
    }

    public static class TagNested<N extends EntandoComponentBundleSpecFluent> extends
            EntandoComponentBundleVersionFluent<TagNested<N>> implements Nested<N> {

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

    public static class AuthorNested<N extends EntandoComponentBundleSpecFluent>
            extends EntandoComponentBundleAuthorFluent<AuthorNested<N>> implements Nested<N> {

        private final N parentBuilder;

        public AuthorNested(N parentBuilder) {
            super();
            this.parentBuilder = parentBuilder;
        }

        @SuppressWarnings("unchecked")
        public N and() {
            return (N) parentBuilder.withAuthor(super.build());
        }

        public N endAuthor() {
            return and();
        }

    }

}
