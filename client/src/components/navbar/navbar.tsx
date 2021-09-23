import * as React from 'react';
import { Button, Modal, Navbar as Bar } from 'react-bootstrap';
import { connect } from 'react-redux';
import { chooseRepositoryTable, chooseLibraryTable, chooseBugTable, REPOSITORY_TABLE, LIBRARY_TABLE, BUG_TABLE, GRAPH_VIEW } from 'reducers/viewChoiceReducer';
import DataLoaderService from 'service/dataLoaderService';

interface Props {
    viewChoice: string
}

interface State {
    aboutModal: boolean,
    dataProtection: boolean,
}

class Navbar extends React.Component<Props, State>  {

    public constructor(props: Props) {
        super(props);
        this.state = {
            aboutModal: false,
            dataProtection: false,
        };
    }

    public render(): JSX.Element {

        const handleRepositoryTableButtonClick = () => {
            //@ts-ignore
            this.props.updateToRepositoryTable();
        }

        const handleLibraryTableButtonClick = () => {
            //@ts-ignore
            this.props.updateToLibraryTable();
        }

        const handleBugTableButtonClick = () => {
            //@ts-ignore
            this.props.updateToBugTable();
        }

        const handleReloadButtonClick = () => {
            const dataPromise = DataLoaderService.getInstance().reloadDataset();
            dataPromise.then(() => window.location.reload());
        }

        const handleAboutButtonClick = () => {
            this.setState({
                aboutModal: true,
                dataProtection: false,
            });
        }

        const handleDataProtectionButtonClick = () => {
            this.setState({
                aboutModal: false,
                dataProtection: true,
            });
        }

        const hideAllModals = () => {
            this.setState({
                aboutModal: false,
                dataProtection: false,
            });
        }

        return (
            <>

                <Modal show={this.state.aboutModal} onHide={hideAllModals} size="lg">
                    <Modal.Header>
                        <Modal.Title>Data Protection</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <h2 style={{ marginBottom: '20px' }}>Contributors</h2>
                        <div style={{ marginBottom: '20px' }} className="text-left">
                            <div><b>Frederik L. Dennig</b></div>
                            <span>Homepage: <a href="https://www.vis.uni-konstanz.de/en/members/dennig/">https://www.vis.uni-konstanz.de/en/members/dennig/</a></span>
                        </div>
                        <div style={{ marginBottom: '20px' }} className="text-left">
                            <div><b>Eren Cakmak</b></div>
                            <span>Homepage: <a href="https://www.vis.uni-konstanz.de/en/members/cakmak/">https://www.vis.uni-konstanz.de/en/members/cakmak/</a></span>
                        </div>
                        <div style={{ marginBottom: '20px' }} className="text-left">
                            <div><b>Henrik Plate</b></div>
                            <span>Homepage: <a href="https://people.sap.com/henrik.plate">https://people.sap.com/henrik.plate</a></span>
                        </div>
                        <div style={{ marginBottom: '20px' }} className="text-left">
                            <div><b>Prof. Dr. Daniel A. Keim</b></div>
                            <span>Homepage: <a href="https://www.vis.uni-konstanz.de/en/members/keim/">https://www.vis.uni-konstanz.de/en/members/keim/</a></span>
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={hideAllModals}>Close</Button>
                    </Modal.Footer>
                </Modal>

                <Modal show={this.state.dataProtection} onHide={hideAllModals} size="lg">
                    <Modal.Header>
                        <Modal.Title>Datenschutzinformationen</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <b>Stand: 27.06.2021</b><br></br>
                        <br></br>
                        <h4>I. Name und Anschrift des Datenschutzbeauftragten</h4>
                        Der Datenschutzbeauftragte des Verantwortlichen ist erreichbar unter:<br></br>
                        E-Mail: <a href="mailto:dsgvo@dbvis.inf.uni-konstanz.de">dsgvo@dbvis.inf.uni-konstanz.de</a>.<br></br>
                        <br></br>
                        <h4>II. Name und Anschrift des Verantwortlichen</h4>
                        Der Verantwortliche im Sinne der Datenschutz-Grundverordnung und anderer nationaler Datenschutzgesetze der Mitgliedstaaten sowie sonstiger datenschutzrechtlicher Bestimmungen ist die:<br></br>
                        <br></br>
                        <a href="https://vis.uni.kn">AG Keim</a><br></br>
                        <a href="https://uni.kn">Universität Konstanz</a><br></br>
                        vertreten durch den Lehrstuhlinhaber, Prof. Dr. Daniel A. Keim<br></br>
                        Universitätsstraße 10<br></br>
                        78464 Konstanz<br></br>
                        Tel. +49 7531 88-3161<br></br>
                        E-Mail: <a href="mailto:keim@uni-konstanz.de">keim@uni-konstanz.de</a><br></br>
                        <br></br>
                        <b>Inhaltliche Verantwortung</b><br></br>
                        Frederik Dennig<br></br>
                        Tel. +49 7531 88-3486<br></br>
                        E-Mail: <a href="mailto:dennig@dbvis.inf.uni-konstanz.de">dennig@dbvis.inf.uni-konstanz.de</a><br></br>
                        <br></br>
                        <h4>III. Bereitstellung der Webseite und Erstellung von Logfiles</h4>
                        <p className="text-justify">
                            <b>1. Beschreibung und Umfang der Datenverarbeitung</b><br></br>
                            Bei jedem Zugriff eines Nutzers oder einer Nutzerin auf eine Seite aus dem Angebot der Website <a href="https://dennig.dbvis.de/vulnex">dennig.dbvis.de/vulnex</a> und bei jedem Abruf einer Datei oder sonstigen Ressource werden folgende Daten über diesen Vorgang in einer Protokolldatei gespeichert:
                        </p>
                        <ul>
                            <li>Datum und Uhrzeit der Anfrage</li>
                            <li>Adresse und Datenmenge (in Bytes) der angefragten und/oder abgerufenen Ressource</li>
                            <li>IP-Adresse der Besucherin / des Besuchers</li>
                            <li>Anfragemethode (z.B. GET-Methode zur Anforderung einer Webseite vom Server)</li>
                            <li>Antwort des Servers (HTTP Statuscode, z. B. „Datei übertragen“ oder „Datei nicht gefunden“ etc.
                            </li>
                            <li>Website, von der aus der Zugriff erfolgte (zuvor besuchte Website, so genannter „Referrer“) und
                                verwendeter Suchmaschinen-Suchbegriff, sofern übermittelt</li>
                            <li>Erkennungsdaten des verwendeten Browser- und Betriebssystems (+ Version), sofern übermittelt
                            </li>
                            <li>Geotargeting (Zuordnung von IP-Adresse zu deren geografischer Herkunft)</li>
                        </ul>
                        <b>2. Rechtsgrundlage</b><br></br>
                        Rechtsgrundlage für die Speicherung der Logfiles ist Art. 6 Abs. 1 lit. e) i.V.m. Abs. 3 DSGVO i.V.m. § 4 Landesdatenschutzgesetz BW (im Folgenden: LDSG) in der ab dem 6. Juni 2018 geltenden Fassung.<br></br>
                        <br></br>
                        <b>3. Zweck der Datenverarbeitung</b><br></br>
                        Die protokollierten Daten werden verwendet, um Störungen oder Fehler an den Systemen, die für das Web-Angebot der Universität Konstanz erforderlich sind, zu erkennen, einzugrenzen oder zu beseitigen. Davon umfasst sind auch Störungen, die zu einer Einschränkung der Verfügbarkeit der Informations- und Kommunikationsdienste oder zu einem unerlaubten Zugriff auf die Systeme führen können.<br></br>
                        <br></br>
                        <b>4. Dauer der Speicherung</b><br></br>
                        Die letzten 2 Bytes der IP-Adresse werde nach maximal 5 Tagen gelöscht, so dass eine Zuordnung der weiteren erhobenen Daten zu einer Person dann nicht mehr möglich ist.<br></br>
                        Die anonymisierten Daten werden für statistische Zwecke verwendet, um die Gestaltung der Website kontinuierlich zu optimieren.<br></br>
                        <br></br>
                        <h4>IV. Verwendung von Cookies</h4>
                        Die Website <a href="https://dennig.dbvis.de/vulnex">dennig.dbvis.de/vulnex</a> erhebt und speichert keine Cookies.
                        <br></br>
                        <br></br>
                        <h4>V. Formulare</h4>
                        Die Website <a href="https://dennig.dbvis.de/vulnex">dennig.dbvis.de/vulnex</a> erhebt und speichert keine personenbezogene Daten in Formularen. Daten (csv-Dateien etc.), die auf die Website hochgeladen werden, um diese mit Hilfe von <a href="https://dennig.dbvis.de/vulnex">dennig.dbvis.de/vulnex</a> zu visualisieren, werden an einen Server übertragen und temporär gespeichert. Nach dem Verlassen der Website werden diese Daten automatisch gelöscht. Die Verantwortlichen der Website haben keine Möglichkeit auf diese Daten zuzugreifen.<br></br>
                        <br></br>
                        <h4>VI. Webanalyse</h4>
                        Die Website <a href="https://dennig.dbvis.de/vulnex">dennig.dbvis.de/vulnex</a> verwendet keine Webanalysetools wie z.B. Google Analytics.<br></br>
                        <br></br>
                        <h4>VII. Rechte der betroffenen Personen</h4>
                        <ol>
                            <li>Sie haben das Recht, von der <a href="https://vis.uni.kn">AG Keim</a> Auskunft über die zu Ihrer Person gespeicherten personenbezogenen Daten gemäß Art. 15 DSGVO zu erhalten und/oder unrichtig gespeicherte personenbezogene Daten gemäß Art. 16 DSGVO berichtigen zu lassen.</li>
                            <li>Sie haben darüber hinaus das Recht auf Löschung (Art. 17 DSGVO) oder auf Einschränkung der Verarbeitung (Art. 18 DSGVO) oder ein Widerspruchsrecht gegen die Verarbeitung (Art. 21 DSGVO).</li>
                            <li>Erfolgt der Widerspruch im Rahmen eines Vertragsverhältnisses kann dies zur Folge haben, dass eine Vertragsdurchführung nicht mehr möglich ist.</li>
                            <li>Beruht die Verarbeitung der personenbezogenen Daten auf einer Einwilligung kann diese jederzeit widerrufen werden. Die Rechtmäßigkeit der Verarbeitung bleibt bis zum Widerruf unberührt.</li>
                            <li>Bitte wenden Sie sich zur Wahrnehmung Ihrer Rechte an den Datenschutzbeauftragten, E-Mail <a href="mailto:dsgvo@dbvis.inf.uni-konstanz.de">dsgvo@dbvis.inf.uni-konstanz.de</a>.</li>
                            <li>Sie haben außerdem das Recht auf Beschwerde bei der Aufsichtsbehörde, wenn Sie der Ansicht sind, dass die Verarbeitung der Sie betreffenden personenbezogenen Daten gegen die datenschutzrechtlichen Vorschriften verstößt (Art. 77 DSGVO). Die zuständige Aufsichtsbehörde ist der Landesbeauftragte für den Datenschutz und die Informationsfreiheit Baden-Württemberg (<a href="https://www.baden-wuerttemberg.datenschutz.de">https://www.baden-wuerttemberg.datenschutz.de</a>)</li>
                        </ol>
                        <h4>VIII. Soziale Netzwerke</h4>
                        Auf der Website <a href="https://dennig.dbvis.de/vulnex">dennig.dbvis.de/vulnex</a> sind keine sozialen Netwerke eingebunden.
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={hideAllModals}>Close</Button>
                    </Modal.Footer>
                </Modal>

                <Bar bg='dark' variant='dark'>
                    <Bar.Brand href='#home'>
                        <img
                            alt=''
                            src={`${process.env.PUBLIC_URL}/logo-rectangle-light.png`}
                            width='125'
                            height='30'
                            className='d-inline-block align-top'
                        />&nbsp;
                        Vulnerability Explorer
                    </Bar.Brand>
                    <Button variant={this.props.viewChoice === REPOSITORY_TABLE ? 'danger' : 'secondary'} onClick={handleRepositoryTableButtonClick}>Repository Table</Button>
                    <div style={{ width: '20px' }}></div>
                    <Button variant={this.props.viewChoice === LIBRARY_TABLE ? 'danger' : 'secondary'} onClick={handleLibraryTableButtonClick}>Library Table</Button>
                    <div style={{ width: '20px' }}></div>
                    <Button variant={this.props.viewChoice === BUG_TABLE ? 'danger' : 'secondary'} onClick={handleBugTableButtonClick}>Bug Table</Button>
                    <div style={{ width: '400px' }}></div>
                    <Button variant='outline-light' onClick={handleReloadButtonClick}>Reload Data</Button>
                    <div style={{ width: '20px' }}></div>
                    <Button variant='outline-light' onClick={handleAboutButtonClick}>About</Button>
                    <div style={{ width: '20px' }}></div>
                    <Button variant='outline-light' onClick={handleDataProtectionButtonClick}>Data Protection</Button>
                </Bar>
            </>
        );
    }
}

const mapStateToProps = (state: any, ownProps: Props) => {
    return { viewChoice: state.viewChoice.current === GRAPH_VIEW ? state.viewChoice.last : state.viewChoice.current };
}

const mapDispatchToProps = (dispatch: any, ownProps: Props) => {
    return {
        updateToRepositoryTable: () => {
            dispatch(chooseRepositoryTable());
        },
        updateToLibraryTable: () => {
            dispatch(chooseLibraryTable());
        },
        updateToBugTable: () => {
            dispatch(chooseBugTable());
        },
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Navbar);
