package com.libgdx.battlearena.ecs.components;

import com.badlogic.ashley.core.ComponentMapper;

public class BAComponentMapper {
    public static ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
    public static ComponentMapper<TransformComponent> transform = ComponentMapper.getFor(TransformComponent.class);

}
