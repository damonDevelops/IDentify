'use strict';

customElements.define('compodoc-menu', class extends HTMLElement {
    constructor() {
        super();
        this.isNormalMode = this.getAttribute('mode') === 'normal';
    }

    connectedCallback() {
        this.render(this.isNormalMode);
    }

    render(isNormalMode) {
        let tp = lithtml.html(`
        <nav>
            <ul class="list">
                <li class="title">
                    <a href="index.html" data-type="index-link">IDentify Documentation</a>
                </li>

                <li class="divider"></li>
                ${ isNormalMode ? `<div id="book-search-input" role="search"><input type="text" placeholder="Type to search"></div>` : '' }
                <li class="chapter">
                    <a data-type="chapter-link" href="index.html"><span class="icon ion-ios-home"></span>Getting started</a>
                    <ul class="links">
                        <li class="link">
                            <a href="overview.html" data-type="chapter-link">
                                <span class="icon ion-ios-keypad"></span>Overview
                            </a>
                        </li>
                        <li class="link">
                            <a href="index.html" data-type="chapter-link">
                                <span class="icon ion-ios-paper"></span>README
                            </a>
                        </li>
                                <li class="link">
                                    <a href="dependencies.html" data-type="chapter-link">
                                        <span class="icon ion-ios-list"></span>Dependencies
                                    </a>
                                </li>
                                <li class="link">
                                    <a href="properties.html" data-type="chapter-link">
                                        <span class="icon ion-ios-apps"></span>Properties
                                    </a>
                                </li>
                    </ul>
                </li>
                    <li class="chapter modules">
                        <a data-type="chapter-link" href="modules.html">
                            <div class="menu-toggler linked" data-bs-toggle="collapse" ${ isNormalMode ?
                                'data-bs-target="#modules-links"' : 'data-bs-target="#xs-modules-links"' }>
                                <span class="icon ion-ios-archive"></span>
                                <span class="link-name">Modules</span>
                                <span class="icon ion-ios-arrow-down"></span>
                            </div>
                        </a>
                        <ul class="links collapse " ${ isNormalMode ? 'id="modules-links"' : 'id="xs-modules-links"' }>
                            <li class="link">
                                <a href="modules/AppModule.html" data-type="entity-link" >AppModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ?
                                            'data-bs-target="#components-links-module-AppModule-3275e7452609b08465911c53bc3f1396dc82b12b89c6061db8576d86e0f37d0433da635164ede212e839e88f1290be6ad562aa065890ec4fe7a0f1ffd553cb0f"' : 'data-bs-target="#xs-components-links-module-AppModule-3275e7452609b08465911c53bc3f1396dc82b12b89c6061db8576d86e0f37d0433da635164ede212e839e88f1290be6ad562aa065890ec4fe7a0f1ffd553cb0f"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-AppModule-3275e7452609b08465911c53bc3f1396dc82b12b89c6061db8576d86e0f37d0433da635164ede212e839e88f1290be6ad562aa065890ec4fe7a0f1ffd553cb0f"' :
                                            'id="xs-components-links-module-AppModule-3275e7452609b08465911c53bc3f1396dc82b12b89c6061db8576d86e0f37d0433da635164ede212e839e88f1290be6ad562aa065890ec4fe7a0f1ffd553cb0f"' }>
                                            <li class="link">
                                                <a href="components/AppComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ?
                                            'data-bs-target="#pipes-links-module-AppModule-3275e7452609b08465911c53bc3f1396dc82b12b89c6061db8576d86e0f37d0433da635164ede212e839e88f1290be6ad562aa065890ec4fe7a0f1ffd553cb0f"' : 'data-bs-target="#xs-pipes-links-module-AppModule-3275e7452609b08465911c53bc3f1396dc82b12b89c6061db8576d86e0f37d0433da635164ede212e839e88f1290be6ad562aa065890ec4fe7a0f1ffd553cb0f"' }>
                                            <span class="icon ion-md-add"></span>
                                            <span>Pipes</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="pipes-links-module-AppModule-3275e7452609b08465911c53bc3f1396dc82b12b89c6061db8576d86e0f37d0433da635164ede212e839e88f1290be6ad562aa065890ec4fe7a0f1ffd553cb0f"' :
                                            'id="xs-pipes-links-module-AppModule-3275e7452609b08465911c53bc3f1396dc82b12b89c6061db8576d86e0f37d0433da635164ede212e839e88f1290be6ad562aa065890ec4fe7a0f1ffd553cb0f"' }>
                                            <li class="link">
                                                <a href="pipes/FilterPipe.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >FilterPipe</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/AppRoutingModule.html" data-type="entity-link" >AppRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/AuthenticationModule.html" data-type="entity-link" >AuthenticationModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ?
                                            'data-bs-target="#components-links-module-AuthenticationModule-82e2fe10d5e855fbd0338b9f2d2ea7b60c1b301470767593f61b898d82ec9fe2cf2066a5a98af1382074d8b92513cca8cb95e62612c029cea2ac69fe8f249349"' : 'data-bs-target="#xs-components-links-module-AuthenticationModule-82e2fe10d5e855fbd0338b9f2d2ea7b60c1b301470767593f61b898d82ec9fe2cf2066a5a98af1382074d8b92513cca8cb95e62612c029cea2ac69fe8f249349"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-AuthenticationModule-82e2fe10d5e855fbd0338b9f2d2ea7b60c1b301470767593f61b898d82ec9fe2cf2066a5a98af1382074d8b92513cca8cb95e62612c029cea2ac69fe8f249349"' :
                                            'id="xs-components-links-module-AuthenticationModule-82e2fe10d5e855fbd0338b9f2d2ea7b60c1b301470767593f61b898d82ec9fe2cf2066a5a98af1382074d8b92513cca8cb95e62612c029cea2ac69fe8f249349"' }>
                                            <li class="link">
                                                <a href="components/AppBoxedForgotPasswordComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppBoxedForgotPasswordComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppBoxedLoginComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppBoxedLoginComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppBoxedRegisterComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppBoxedRegisterComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppBoxedTwoStepsComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppBoxedTwoStepsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppErrorComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppErrorComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppMaintenanceComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppMaintenanceComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppSideForgotPasswordComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppSideForgotPasswordComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppSideLoginComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppSideLoginComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppSideRegisterComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppSideRegisterComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppSideTwoStepsComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppSideTwoStepsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/DeactivatedCollectionPointDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >DeactivatedCollectionPointDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/HowItWorksComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >HowItWorksComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/DashboardsModule.html" data-type="entity-link" >DashboardsModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ?
                                            'data-bs-target="#components-links-module-DashboardsModule-4cb5e80b23052e8472dc60295388961c19ab4d2c1e14e0e36f0905001a3393b04518a8a4bdfdae355aa40f6d92c928230ea9bff5f408995aad1464af5b3555af"' : 'data-bs-target="#xs-components-links-module-DashboardsModule-4cb5e80b23052e8472dc60295388961c19ab4d2c1e14e0e36f0905001a3393b04518a8a4bdfdae355aa40f6d92c928230ea9bff5f408995aad1464af5b3555af"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-DashboardsModule-4cb5e80b23052e8472dc60295388961c19ab4d2c1e14e0e36f0905001a3393b04518a8a4bdfdae355aa40f6d92c928230ea9bff5f408995aad1464af5b3555af"' :
                                            'id="xs-components-links-module-DashboardsModule-4cb5e80b23052e8472dc60295388961c19ab4d2c1e14e0e36f0905001a3393b04518a8a4bdfdae355aa40f6d92c928230ea9bff5f408995aad1464af5b3555af"' }>
                                            <li class="link">
                                                <a href="components/AppDashboard1Component.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppDashboard1Component</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/DashboardWidgetsModule.html" data-type="entity-link" >DashboardWidgetsModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ?
                                            'data-bs-target="#components-links-module-DashboardWidgetsModule-ece00fb3ebaee268db87f9faf6c2a52b2f2d41305fbb07f67107065b51b5805689a1682e31f64f2cbedada20f7934c802a986faba9ca4e9ea7fbb9221f57378a"' : 'data-bs-target="#xs-components-links-module-DashboardWidgetsModule-ece00fb3ebaee268db87f9faf6c2a52b2f2d41305fbb07f67107065b51b5805689a1682e31f64f2cbedada20f7934c802a986faba9ca4e9ea7fbb9221f57378a"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-DashboardWidgetsModule-ece00fb3ebaee268db87f9faf6c2a52b2f2d41305fbb07f67107065b51b5805689a1682e31f64f2cbedada20f7934c802a986faba9ca4e9ea7fbb9221f57378a"' :
                                            'id="xs-components-links-module-DashboardWidgetsModule-ece00fb3ebaee268db87f9faf6c2a52b2f2d41305fbb07f67107065b51b5805689a1682e31f64f2cbedada20f7934c802a986faba9ca4e9ea7fbb9221f57378a"' }>
                                            <li class="link">
                                                <a href="components/AppAccountSettingsRedirectComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppAccountSettingsRedirectComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppCompanyManagementRedirectComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppCompanyManagementRedirectComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppFormManagementRedirectComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppFormManagementRedirectComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppQRCodeGenerationRedirectComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppQRCodeGenerationRedirectComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppScanHistoryRedirectWidget.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppScanHistoryRedirectWidget</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppScanIdRedirectComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppScanIdRedirectComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/GablesModule.html" data-type="entity-link" >GablesModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ?
                                            'data-bs-target="#components-links-module-GablesModule-8375c573e6a87def2a8ecdf47d5695a9c334a6c996f6ee326f0c877cb9dfea2c5cddc8379a52d74b303d628887f77c9c09cce0ea300f690d6761ae76f612af72"' : 'data-bs-target="#xs-components-links-module-GablesModule-8375c573e6a87def2a8ecdf47d5695a9c334a6c996f6ee326f0c877cb9dfea2c5cddc8379a52d74b303d628887f77c9c09cce0ea300f690d6761ae76f612af72"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-GablesModule-8375c573e6a87def2a8ecdf47d5695a9c334a6c996f6ee326f0c877cb9dfea2c5cddc8379a52d74b303d628887f77c9c09cce0ea300f690d6761ae76f612af72"' :
                                            'id="xs-components-links-module-GablesModule-8375c573e6a87def2a8ecdf47d5695a9c334a6c996f6ee326f0c877cb9dfea2c5cddc8379a52d74b303d628887f77c9c09cce0ea300f690d6761ae76f612af72"' }>
                                            <li class="link">
                                                <a href="components/AppScanIdComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppScanIdComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CommonScanIdComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >CommonScanIdComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/DesktopQrCodeScanIdComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >DesktopQrCodeScanIdComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/FailedJobsDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >FailedJobsDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/MobileScanIdComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >MobileScanIdComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/NoCollectionPointsDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >NoCollectionPointsDialogComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/LandingPageModule.html" data-type="entity-link" >LandingPageModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ?
                                            'data-bs-target="#components-links-module-LandingPageModule-748500c5404c352625e93f79a1d37748dc1cf9d5dc2ee7311fdca8a5b6f5ffcbbf5b027302275e813b12e6f91b5af86cc0b765ed0e80299e7fe7d38e958c1ace"' : 'data-bs-target="#xs-components-links-module-LandingPageModule-748500c5404c352625e93f79a1d37748dc1cf9d5dc2ee7311fdca8a5b6f5ffcbbf5b027302275e813b12e6f91b5af86cc0b765ed0e80299e7fe7d38e958c1ace"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-LandingPageModule-748500c5404c352625e93f79a1d37748dc1cf9d5dc2ee7311fdca8a5b6f5ffcbbf5b027302275e813b12e6f91b5af86cc0b765ed0e80299e7fe7d38e958c1ace"' :
                                            'id="xs-components-links-module-LandingPageModule-748500c5404c352625e93f79a1d37748dc1cf9d5dc2ee7311fdca8a5b6f5ffcbbf5b027302275e813b12e6f91b5af86cc0b765ed0e80299e7fe7d38e958c1ace"' }>
                                            <li class="link">
                                                <a href="components/AppLandingpageComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppLandingpageComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/MaterialModule.html" data-type="entity-link" >MaterialModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/SharedModule.html" data-type="entity-link" >SharedModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ?
                                            'data-bs-target="#components-links-module-SharedModule-e58d41a27ed168187aefdc9d75f054eb711350e90119e3070d709c0ad4669901be0c6f15e4b4e3a8fb38c3696e33eaabdce119b2b6cd20551624d47fc0324e94"' : 'data-bs-target="#xs-components-links-module-SharedModule-e58d41a27ed168187aefdc9d75f054eb711350e90119e3070d709c0ad4669901be0c6f15e4b4e3a8fb38c3696e33eaabdce119b2b6cd20551624d47fc0324e94"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-SharedModule-e58d41a27ed168187aefdc9d75f054eb711350e90119e3070d709c0ad4669901be0c6f15e4b4e3a8fb38c3696e33eaabdce119b2b6cd20551624d47fc0324e94"' :
                                            'id="xs-components-links-module-SharedModule-e58d41a27ed168187aefdc9d75f054eb711350e90119e3070d709c0ad4669901be0c6f15e4b4e3a8fb38c3696e33eaabdce119b2b6cd20551624d47fc0324e94"' }>
                                            <li class="link">
                                                <a href="components/CheckingIpsDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >CheckingIpsDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ConsentDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >ConsentDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ExpandDataModalMobileComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >ExpandDataModalMobileComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/RedirectDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >RedirectDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/TimeoutDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >TimeoutDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/WelcomeDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >WelcomeDialogComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/TablesModule.html" data-type="entity-link" >TablesModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ?
                                            'data-bs-target="#components-links-module-TablesModule-565fc287078f5ad7b8977e0d2646a52c0ec8c2b00c084f65da54148df191ff4d98cf1a146571f7ba662514b8d75d46a2cfb9a4a105aee1942f9924220c582a68"' : 'data-bs-target="#xs-components-links-module-TablesModule-565fc287078f5ad7b8977e0d2646a52c0ec8c2b00c084f65da54148df191ff4d98cf1a146571f7ba662514b8d75d46a2cfb9a4a105aee1942f9924220c582a68"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-TablesModule-565fc287078f5ad7b8977e0d2646a52c0ec8c2b00c084f65da54148df191ff4d98cf1a146571f7ba662514b8d75d46a2cfb9a4a105aee1942f9924220c582a68"' :
                                            'id="xs-components-links-module-TablesModule-565fc287078f5ad7b8977e0d2646a52c0ec8c2b00c084f65da54148df191ff4d98cf1a146571f7ba662514b8d75d46a2cfb9a4a105aee1942f9924220c582a68"' }>
                                            <li class="link">
                                                <a href="components/AppMixTableComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppMixTableComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ExpandedDataModalComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >ExpandedDataModalComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/FormDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >FormDialogComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/ThemePagesModule.html" data-type="entity-link" >ThemePagesModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ?
                                            'data-bs-target="#components-links-module-ThemePagesModule-24f5fa797e0a7a04c342ffbe675886218544e1aecda27985dc0952e84b7d09f543fb8eb0b74e955ae4cb258a818c15bf4a665fb6d544aeaf50a43352e33b4bde"' : 'data-bs-target="#xs-components-links-module-ThemePagesModule-24f5fa797e0a7a04c342ffbe675886218544e1aecda27985dc0952e84b7d09f543fb8eb0b74e955ae4cb258a818c15bf4a665fb6d544aeaf50a43352e33b4bde"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-ThemePagesModule-24f5fa797e0a7a04c342ffbe675886218544e1aecda27985dc0952e84b7d09f543fb8eb0b74e955ae4cb258a818c15bf4a665fb6d544aeaf50a43352e33b4bde"' :
                                            'id="xs-components-links-module-ThemePagesModule-24f5fa797e0a7a04c342ffbe675886218544e1aecda27985dc0952e84b7d09f543fb8eb0b74e955ae4cb258a818c15bf4a665fb6d544aeaf50a43352e33b4bde"' }>
                                            <li class="link">
                                                <a href="components/AccountSettingComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AccountSettingComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AddUserDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AddUserDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CollectionPointComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >CollectionPointComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CompanyManagementComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >CompanyManagementComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CreateCompanyComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >CreateCompanyComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/CreateNewFormDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >CreateNewFormDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/DeleteFormDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >DeleteFormDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/FormManagementComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >FormManagementComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/FormsTableComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >FormsTableComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/HowDoesThisWorkDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >HowDoesThisWorkDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/InviteUsersComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >InviteUsersComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/UpdateEmailDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >UpdateEmailDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/UpdateFormDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >UpdateFormDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/UpdatePasswordDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >UpdatePasswordDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ViewUsersComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >ViewUsersComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                </ul>
                </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ? 'data-bs-target="#components-links"' :
                            'data-bs-target="#xs-components-links"' }>
                            <span class="icon ion-md-cog"></span>
                            <span>Components</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="components-links"' : 'id="xs-components-links"' }>
                            <li class="link">
                                <a href="components/AppBreadcrumbComponent.html" data-type="entity-link" >AppBreadcrumbComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/AppHorizontalHeaderComponent.html" data-type="entity-link" >AppHorizontalHeaderComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/AppHorizontalNavItemComponent.html" data-type="entity-link" >AppHorizontalNavItemComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/AppHorizontalSearchDialogComponent.html" data-type="entity-link" >AppHorizontalSearchDialogComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/AppHorizontalSidebarComponent.html" data-type="entity-link" >AppHorizontalSidebarComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/AppNavItemComponent.html" data-type="entity-link" >AppNavItemComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/BlankComponent.html" data-type="entity-link" >BlankComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/BrandingComponent.html" data-type="entity-link" >BrandingComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/CollectionPointDeleteDialog.html" data-type="entity-link" >CollectionPointDeleteDialog</a>
                            </li>
                            <li class="link">
                                <a href="components/CustomizerComponent.html" data-type="entity-link" >CustomizerComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/DialogAnimationsExampleDialog.html" data-type="entity-link" >DialogAnimationsExampleDialog</a>
                            </li>
                            <li class="link">
                                <a href="components/DialogAnimationsExampleDialog-1.html" data-type="entity-link" >DialogAnimationsExampleDialog</a>
                            </li>
                            <li class="link">
                                <a href="components/FullComponent.html" data-type="entity-link" >FullComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/HeaderComponent.html" data-type="entity-link" >HeaderComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/MobileScanIdComponent-1.html" data-type="entity-link" >MobileScanIdComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/SidebarComponent.html" data-type="entity-link" >SidebarComponent</a>
                            </li>
                            <li class="link">
                                <a href="components/UsersComponent.html" data-type="entity-link" >UsersComponent</a>
                            </li>
                        </ul>
                    </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ? 'data-bs-target="#classes-links"' :
                            'data-bs-target="#xs-classes-links"' }>
                            <span class="icon ion-ios-paper"></span>
                            <span>Classes</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="classes-links"' : 'id="xs-classes-links"' }>
                            <li class="link">
                                <a href="classes/ApiUserDataSource.html" data-type="entity-link" >ApiUserDataSource</a>
                            </li>
                            <li class="link">
                                <a href="classes/Refresh.html" data-type="entity-link" >Refresh</a>
                            </li>
                            <li class="link">
                                <a href="classes/User.html" data-type="entity-link" >User</a>
                            </li>
                        </ul>
                    </li>
                        <li class="chapter">
                            <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ? 'data-bs-target="#injectables-links"' :
                                'data-bs-target="#xs-injectables-links"' }>
                                <span class="icon ion-md-arrow-round-down"></span>
                                <span>Injectables</span>
                                <span class="icon ion-ios-arrow-down"></span>
                            </div>
                            <ul class="links collapse " ${ isNormalMode ? 'id="injectables-links"' : 'id="xs-injectables-links"' }>
                                <li class="link">
                                    <a href="injectables/AbnSearchService.html" data-type="entity-link" >AbnSearchService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ApiService.html" data-type="entity-link" >ApiService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/AuthenticationService.html" data-type="entity-link" >AuthenticationService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/AuthoritiesService.html" data-type="entity-link" >AuthoritiesService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/CommunicationService.html" data-type="entity-link" >CommunicationService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/CompanyServiceService.html" data-type="entity-link" >CompanyServiceService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/CoreService.html" data-type="entity-link" >CoreService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/CryptoUtilService.html" data-type="entity-link" >CryptoUtilService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/IntroService.html" data-type="entity-link" >IntroService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/NavService.html" data-type="entity-link" >NavService</a>
                                </li>
                            </ul>
                        </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ? 'data-bs-target="#interceptors-links"' :
                            'data-bs-target="#xs-interceptors-links"' }>
                            <span class="icon ion-ios-swap"></span>
                            <span>Interceptors</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="interceptors-links"' : 'id="xs-interceptors-links"' }>
                            <li class="link">
                                <a href="interceptors/ErrorInterceptor.html" data-type="entity-link" >ErrorInterceptor</a>
                            </li>
                            <li class="link">
                                <a href="interceptors/JwtInterceptor.html" data-type="entity-link" >JwtInterceptor</a>
                            </li>
                        </ul>
                    </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ? 'data-bs-target="#guards-links"' :
                            'data-bs-target="#xs-guards-links"' }>
                            <span class="icon ion-ios-lock"></span>
                            <span>Guards</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="guards-links"' : 'id="xs-guards-links"' }>
                            <li class="link">
                                <a href="guards/AuthGuard.html" data-type="entity-link" >AuthGuard</a>
                            </li>
                        </ul>
                    </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ? 'data-bs-target="#interfaces-links"' :
                            'data-bs-target="#xs-interfaces-links"' }>
                            <span class="icon ion-md-information-circle-outline"></span>
                            <span>Interfaces</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? ' id="interfaces-links"' : 'id="xs-interfaces-links"' }>
                            <li class="link">
                                <a href="interfaces/AbnSearchResult.html" data-type="entity-link" >AbnSearchResult</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/AccessListUser.html" data-type="entity-link" >AccessListUser</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/ApiResponse.html" data-type="entity-link" >ApiResponse</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/ApiResponse-1.html" data-type="entity-link" >ApiResponse</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/ApiUser.html" data-type="entity-link" >ApiUser</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/apps.html" data-type="entity-link" >apps</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/AppSettings.html" data-type="entity-link" >AppSettings</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/breadcrumbOption.html" data-type="entity-link" >breadcrumbOption</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/CollectionPoint.html" data-type="entity-link" >CollectionPoint</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/CollectionPoint-1.html" data-type="entity-link" >CollectionPoint</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/Company.html" data-type="entity-link" >Company</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/demos.html" data-type="entity-link" >demos</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/DialogData.html" data-type="entity-link" >DialogData</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/features.html" data-type="entity-link" >features</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/Form.html" data-type="entity-link" >Form</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/Medicare.html" data-type="entity-link" >Medicare</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/msgs.html" data-type="entity-link" >msgs</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/NavItem.html" data-type="entity-link" >NavItem</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/NewApiResponse.html" data-type="entity-link" >NewApiResponse</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/NewApiResponse-1.html" data-type="entity-link" >NewApiResponse</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/notifications.html" data-type="entity-link" >notifications</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/NSWDriversLicense.html" data-type="entity-link" >NSWDriversLicense</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/NSWDriversLicenseProvisional.html" data-type="entity-link" >NSWDriversLicenseProvisional</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/NSWPhotoCard.html" data-type="entity-link" >NSWPhotoCard</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/profiledd.html" data-type="entity-link" >profiledd</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/profiledd-1.html" data-type="entity-link" >profiledd</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/quicklinks.html" data-type="entity-link" >quicklinks</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/testimonials.html" data-type="entity-link" >testimonials</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/UowId.html" data-type="entity-link" >UowId</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/User.html" data-type="entity-link" >User</a>
                            </li>
                        </ul>
                    </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ? 'data-bs-target="#miscellaneous-links"'
                            : 'data-bs-target="#xs-miscellaneous-links"' }>
                            <span class="icon ion-ios-cube"></span>
                            <span>Miscellaneous</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="miscellaneous-links"' : 'id="xs-miscellaneous-links"' }>
                            <li class="link">
                                <a href="miscellaneous/enumerations.html" data-type="entity-link">Enums</a>
                            </li>
                            <li class="link">
                                <a href="miscellaneous/functions.html" data-type="entity-link">Functions</a>
                            </li>
                            <li class="link">
                                <a href="miscellaneous/variables.html" data-type="entity-link">Variables</a>
                            </li>
                        </ul>
                    </li>
                        <li class="chapter">
                            <a data-type="chapter-link" href="routes.html"><span class="icon ion-ios-git-branch"></span>Routes</a>
                        </li>
                    <li class="chapter">
                        <a data-type="chapter-link" href="coverage.html"><span class="icon ion-ios-stats"></span>Documentation coverage</a>
                    </li>
                    <li class="divider"></li>
                    <li class="copyright">
                        Documentation generated using <a href="https://compodoc.app/" target="_blank" rel="noopener noreferrer">
                            <img data-src="images/compodoc-vectorise.png" class="img-responsive" data-type="compodoc-logo">
                        </a>
                    </li>
            </ul>
        </nav>
        `);
        this.innerHTML = tp.strings;
    }
});