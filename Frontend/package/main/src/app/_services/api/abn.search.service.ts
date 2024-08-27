import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AbnSearchService {
  private abnUrl = 'https://abr.business.gov.au/abrxmlsearch/AbrXmlSearch.asmx';

  constructor(private http: HttpClient) {}

  // GET request example
  searchByABN(
    guid: string,
    abn: string,
    includeHistorical: boolean,
  ): Observable<AbnSearchResult> {
    const params = new HttpParams()
      .set('authenticationGuid', guid)
      .set('searchString', abn)
      .set(
        'includeHistoricalDetails',
        this.encodeBooleanParam(includeHistorical),
      );

    return this.http
      .get(`${this.abnUrl}/ABRSearchByABN`, { params, responseType: 'text' })
      .pipe(
        map((xmlResponse) => {
          const parser = new DOMParser();
          const xmlDoc = parser.parseFromString(xmlResponse, 'text/xml');
          const abnElement = xmlDoc.querySelector(
            'ABRPayloadSearchResults > response > businessEntity',
          );

          if (abnElement) {
            const abnSearchResult: AbnSearchResult = {
              ABN:
                abnElement.querySelector('ABN > identifierValue')
                  ?.textContent || '',
              entityStatus: {
                entityStatusCode:
                  abnElement.querySelector('entityStatus > entityStatusCode')
                    ?.textContent || '',
                effectiveFrom:
                  abnElement.querySelector('entityStatus > effectiveFrom')
                    ?.textContent || '',
                effectiveTo:
                  abnElement.querySelector('entityStatus > effectiveTo')
                    ?.textContent || '',
              },
              ASICNumber:
                abnElement.querySelector('ASICNumber')?.textContent || '',
              entityType: {
                entityTypeCode:
                  abnElement.querySelector('entityType > entityTypeCode')
                    ?.textContent || '',
                entityDescription:
                  abnElement.querySelector('entityType > entityDescription')
                    ?.textContent || '',
              },
              mainName: {
                organisationName:
                  abnElement.querySelector('mainName > organisationName')
                    ?.textContent || '',
                effectiveFrom:
                  abnElement.querySelector('mainName > effectiveFrom')
                    ?.textContent || '',
              },
              mainBusinessPhysicalAddress: {
                stateCode:
                  abnElement.querySelector(
                    'mainBusinessPhysicalAddress > stateCode',
                  )?.textContent || '',
                postcode:
                  abnElement.querySelector(
                    'mainBusinessPhysicalAddress > postcode',
                  )?.textContent || '',
                effectiveFrom:
                  abnElement.querySelector(
                    'mainBusinessPhysicalAddress > effectiveFrom',
                  )?.textContent || '',
                effectiveTo:
                  abnElement.querySelector(
                    'mainBusinessPhysicalAddress > effectiveTo',
                  )?.textContent || '',
              },
              // Add more fields as needed
            };
            return abnSearchResult;
          } else {
            throw new Error('ABN not found in the XML response');
          }
        }),
      );
  }

  private encodeBooleanParam(value: boolean): string {
    return value ? 'Y' : 'N';
  }
}

interface AbnSearchResult {
  ABN: string;
  entityStatus: {
    entityStatusCode: string;
    effectiveFrom: string;
    effectiveTo: string;
  };
  ASICNumber: string;
  entityType: {
    entityTypeCode: string;
    entityDescription: string;
  };
  mainName: {
    organisationName: string;
    effectiveFrom: string;
  };
  mainBusinessPhysicalAddress: {
    stateCode: string;
    postcode: string;
    effectiveFrom: string;
    effectiveTo: string;
  };
  // Add more fields as needed
}
