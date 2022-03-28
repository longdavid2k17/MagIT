package pl.kantoch.dawid.magit.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TeamsService
{
    public ResponseEntity<?> getAllTeamsInOrganisation(Long id)
    {
        return ResponseEntity.ok().build();
    }
}
