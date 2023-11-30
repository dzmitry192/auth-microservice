package com.innowise.authmicroservice.mapper;

import com.innowise.authmicroservice.dto.ClientDto;
import com.innowise.authmicroservice.dto.CreateClientDto;
import com.innowise.authmicroservice.dto.UpdateClientDto;
import com.innowise.authmicroservice.entity.ClientEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = ClientMapper.class)
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    ClientEntity createClientDtoToClientEntity(CreateClientDto createClientDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClientEntity updateClientDtoToClientEntity(UpdateClientDto updateClientDto, @MappingTarget ClientEntity clientEntity);
    ClientDto clientEntityToClientDto(ClientEntity clientEntity);
    List<ClientDto> toClientDtoList(List<ClientEntity> clientEntities);
}
