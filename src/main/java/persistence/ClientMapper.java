package persistence;

import upm.users.Client;
import upm.users.TypeClient;

public class ClientMapper {

    public static ClientDTO toDTO(Client client) {  //Convertir de cliente en clienteDTO
        ClientDTO dto = new ClientDTO();
        dto.name = client.getName();
        dto.dni = client.getId();
        dto.email = client.getEmail();
        dto.cashId = client.getCashId();
        return dto;
    }



    public static Client fromDTO(ClientDTO dto) {  //Convertir de clienteDTO a cliente cuando cargas
        return new Client(
                dto.name,
                dto.dni,
                dto.email,
                dto.cashId,
                TypeClient.valueOf(dto.type)
        );
    }
}

